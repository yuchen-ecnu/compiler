package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.lexer.Lexer;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.parser.LLParser;
import com.ecnu.compiler.component.parser.LRParser;
import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.component.semantic.SemanticAnalyzer;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;

import java.util.List;

/**
 * 编译器  --定义对外的所有接口
 * （最终作为Jar包导入WEB程序中）
 *
 * @author Michael Chen
 * @date 2018/05/01
 */
public class Compiler {
    //存储
    /** 控制器需要的语言信息 */
    private Language mLanguage;

    /** 异常列表 */
    private ErrorList mErrorList;
    /** 时间表 */
    private Compiler.TimeHolder mTimeHolder;

    /** 编译状态码 */
    private StatusCode mStatus;
    /** 配置参数 */
    private Config mConfig;

    /** 预处理器 */
    private Preprocessor mPreprocessor;
    /** 词法分析器 */
    private Lexer mLexer;
    /** 语法分析器 */
    private Parser mParser;
    /** 语义分析器 */
    protected SemanticAnalyzer mSemanticAnalyzer;
    /** Backend处理器 */

    //运行变量
    //当前进行编译的代码内容
    private String mTextToCompiler;
    //预处理之后的内容
    private List<Token> mTextListAfterPreprocess;
    //词法分析器得到的符号表
    private SymbolTable mSymbolTable;
    //语法分析后得到的语法树,以及语义分析后得到的注释语法树
    private TD mSyntaxTree;
    //语法分析过程的记录表
    private PredictTable mPredictTable;
    //语义分析后得到的动作列表
    private List<String> mActionList;


    /**
     * 根据传入语言构建编译器
     */
    public Compiler(Language language, Config config, ErrorList errorList){
        //初始化变量
        mStatus = StatusCode.STAGE_INIT;
        mErrorList = errorList;
        mConfig = config;
        mLanguage = language;
        mTimeHolder = new TimeHolder();

        //创建编译器各部件
        //创建预处理器，由子类创建
        mPreprocessor = new Preprocessor(Constants.ANNOTATION);
        //创建词法分析器，固定创建Lexer
        //mLexer = createLexer(language.getDFAList());
        mLexer = new Lexer(language.getREList(), mErrorList, 0);
        //创建语法分析器，根据config创建
        mParser = createParser(language, mErrorList);
        //创建语义分析器
        mSemanticAnalyzer = new SemanticAnalyzer(language.getAG(), mErrorList);

        mStatus = StatusCode.RUNNING;
    }


    /**
     * 根据配置文件执行一步
     * @return 编译器当前状态码（参见状态说明文档）
     */

    public StatusCode getStatus() {
        return mStatus;
    }


    /**
     * 获取时间表
     * @return 时间表
     */
    public TimeHolder getTimeHolder() {
        return mTimeHolder;
    }


    /**
     * 获得生成的符号表
     * @return 符号表，可能为null
     */
    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    /**
     * 获得生成的语法树
     * @return
     */
    public TD getSyntaxTree() {
        return mSyntaxTree;
    }

    /**
     * 获得语义分析得到的动作列表
     * @return
     */
    public List<String> getActionList() {
        return mActionList;
    }


    /**
     * 获得语法分析过程中的分析表
     * @return
     */
    public PredictTable getPredictTable() {
        return mPredictTable;
    }

    /**
     * 获取错误列表
     * @return 错误列表
     */
    public ErrorList getErrorList() {
        return mErrorList;
    }



    public StatusCode prepare(String text){
        mTextToCompiler = text;
        mStatus = StatusCode.STAGE_PREPROCESSOR;
        return mStatus;
    }

    /** 执行下一步程序 */
    public StatusCode next() {
        switch(mConfig.getExecuteType()){
            //全部执行
            case Constants.EXECUTE_IN_ONE_STEP:
                break;
            //单阶段执行
            case Constants.EXECUTE_STAGE_BY_STAGE:
                nextStep();
                break;
            //单步执行
            case Constants.EXECUTE_STEP_BY_STEP:
                break;
            default:
                return StatusCode.ERROR;
        }
        return mStatus;
    }

    /** 单步执行 */
    private StatusCode nextStep(){
        long startTime = System.currentTimeMillis();
        switch (mStatus){
            case STAGE_PREPROCESSOR:
                mTextListAfterPreprocess = mPreprocessor.preprocess(mTextToCompiler);
                mStatus = StatusCode.STAGE_LEXER;
                //计时
                mTimeHolder.setPreprocessorTime(System.currentTimeMillis() - startTime);
                break;
            case STAGE_LEXER:
                if (mTextListAfterPreprocess != null){
                    mSymbolTable = new SymbolTable();
                    boolean result = mLexer.buildSymbolTable(mTextListAfterPreprocess, mSymbolTable);
                    mStatus = result ? StatusCode.STAGE_PARSER : StatusCode.ERROR;
                    //计时
                    mTimeHolder.setLexerTime(System.currentTimeMillis() - startTime);
                } else {
                    mStatus = StatusCode.ERROR;
                    mErrorList.addErrorMsg("预处理内部错误", StatusCode.ERROR_PREPROCESSOR);
                }
                break;
            case STAGE_PARSER:
                if (mSymbolTable != null){
                    mPredictTable = new PredictTable();
                    mSyntaxTree = mParser.buildSyntaxTree(getSymbolTable(), mPredictTable);
                    if (mSyntaxTree == null){
                        mStatus = StatusCode.ERROR;
                        break;
                    }
                    mStatus = StatusCode.STAGE_SEMANTIC_ANALYZER;
                    //计时
                    mTimeHolder.setParserTime(System.currentTimeMillis() - startTime);
                } else {
                    mStatus = StatusCode.ERROR;
                    mErrorList.addErrorMsg("词法分析内部错误", StatusCode.ERROR_LEXER);
                }
                break;
            case STAGE_SEMANTIC_ANALYZER:
                if (mSyntaxTree != null){
                    mActionList = mSemanticAnalyzer.buildAttrubuteTree(mSyntaxTree);
                    if (mActionList == null){
                        mStatus = StatusCode.ERROR;
                        break;
                    }
                    mStatus = StatusCode.STAGE_BACKEND;
                    //计时
                    mTimeHolder.setSemanticTime(System.currentTimeMillis() - startTime);
                } else {
                    mStatus = StatusCode.ERROR;
                    mErrorList.addErrorMsg("语法分析内部错误", StatusCode.ERROR_PARSER);
                }
                break;
            case STAGE_BACKEND:
                mStatus = StatusCode.ERROR;
                mErrorList.addErrorMsg("后端操作未实现", StatusCode.ERROR);
                break;
            case STAGE_FINISHED:
                mStatus = StatusCode.ERROR;
                mErrorList.addErrorMsg("后端操作未实现", StatusCode.ERROR);
                break;
            default:
                break;
        }
        return mStatus;
    }

    /**
     * 创建词法分析器
     * @return 词法分析器
     */
    private Lexer createLexer(List<DFA> dfaList) {
        return new Lexer(dfaList, mErrorList);
    }

    /**
     * 创建语法分析器
     * @return 对应算法的语法分析器
     */
    private Parser createParser(Language language, ErrorList errorList) {
        switch(mConfig.getParserAlgorithm()){
            case Constants.PARSER_LL:
                return new LLParser(language.getCFG(), language.getLLParsingTable(), errorList);
            case Constants.PARSER_LR:
                return new LRParser(language.getCFG(), language.getLRParsingTable(), errorList);
            case Constants.PARSER_SLR:
                return new LRParser(language.getCFG(), language.getSLRParsingTable(), errorList);
            case Constants.PARSER_LALR:
                return new LRParser(language.getCFG(), language.getLALRParsingTable(), errorList);
            default:
                mErrorList.addErrorMsg("配置错误：使用了未知的语法分析算法,使用默认LL继续运行", StatusCode.ERROR_INIT);
                mStatus = StatusCode.ERROR_INIT;
                return null;
        }
    }


    public class TimeHolder {
        //预处理时间
        private long preprocessorTime;
        //词法分析时间
        private long lexerTime;
        //语法分析时间
        private long parserTime;
        //语义分析时间
        private long semanticTime;

        public long getPreprocessorTime() {
            return preprocessorTime;
        }

        void setPreprocessorTime(long preprocessorTime) {
            this.preprocessorTime = preprocessorTime;
        }

        public long getLexerTime() {
            return lexerTime;
        }

        void setLexerTime(long lexerTime) {
            this.lexerTime = lexerTime;
        }

        public long getParserTime() {
            return parserTime;
        }

        void setParserTime(long parserTime) {
            this.parserTime = parserTime;
        }

        public long getSemanticTime() {
            return semanticTime;
        }

        public void setSemanticTime(long semanticTime) {
            this.semanticTime = semanticTime;
        }
    }
}
