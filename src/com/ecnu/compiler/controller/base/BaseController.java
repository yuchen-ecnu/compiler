package com.ecnu.compiler.controller.base;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.lexer.Lexer;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.parser.LLParser;
import com.ecnu.compiler.component.parser.LRParser;
import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.component.semantic.SemanticAnalyzer;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;

import java.util.List;

/**
 * 控制器 基类（控制器行为集合）
 * @author Michael Chen
 */
public abstract class BaseController {

    //存储
    /** 控制器需要的语言信息 */
    protected Language mLanguage;

    /** 异常列表 */
    protected ErrorList mErrorList;

    /** 编译状态码 */
    protected StatusCode mStatus;
    /** 配置参数 */
    protected Config mConfig;

    /** 预处理器 */
    protected Preprocessor mPreprocessor;
    /** 词法分析器 */
    protected Lexer mLexer;
    /** 语法分析器 */
    protected Parser mParser;
    /** 语义分析器 */
    protected SemanticAnalyzer mSemanticAnalyzer;
    /** Backend处理器 */

    //运行变量
    //当前进行编译的代码内容
    private String mTextToCompiler;
    //预处理之后的内容
    private List<String> mTextListAfterPreprocess;
    //词法分析器得到的符号表
    private SymbolTable mSymbolTable;
    //语法分析后得到的语法树

    public BaseController(Language language, Config config, ErrorList errorList) {
        //初始化变量
        mStatus = StatusCode.STAGE_INIT;
        mLanguage = language;
        mConfig = config;
        mErrorList = errorList;
        //创建编译器各部件
        //创建预处理器，由子类创建
        mPreprocessor = createPreprocessor();
        //创建词法分析器，固定创建Lexer
        //mLexer = createLexer(language.getDFAList());
        mLexer = new Lexer(language.getREList(), 0);
        //创建语法分析器，根据config创建
        mParser = createParser(language);
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

    /**
     * 获得生成的符号表
     * @return 符号表，可能为null
     */
    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    /** 单步执行 */
    private StatusCode nextStep(){
        switch (mStatus){
            case ERROR:
                break;
            case STAGE_PREPROCESSOR:
                if (!"".equals(mTextToCompiler)){
                    mTextListAfterPreprocess = mPreprocessor.preprocess(mTextToCompiler);
                    mStatus = StatusCode.STAGE_LEXER;
                }
                break;
            case STAGE_LEXER:
                if (mTextListAfterPreprocess != null){
                    mSymbolTable = mLexer.buildSymbolTable(mTextListAfterPreprocess);
                    mStatus = StatusCode.STAGE_PARSER;
                }
                break;
            case STAGE_PARSER:
                break;
            case STAGE_SEMANTIC_ANALYZER:
                break;
            case STAGE_BACKEND:
                break;
            case STAGE_FINISHED:
                break;
            default:
                break;
        }
        return mStatus;
    }

    /**
     * 创建词法分析器
     * @return 对应算法的词法分析器
     */
    protected abstract Preprocessor createPreprocessor();

    /**
     * 创建词法分析器
     * @return 词法分析器
     */
    private Lexer createLexer(List<DFA> dfaList) {
        return new Lexer(dfaList);
    }

    /**
     * 创建语法分析器
     * @return 对应算法的语法分析器
     */
    private Parser createParser(Language language) {
        switch(mConfig.getParserAlgorithm()){
            case Constants.PARSER_LL:
                return new LLParser(language.getCFG(), language.getLLParsingTable());
            case Constants.PARSER_LR:
                return new LRParser(language.getCFG(), language.getLRParsingTable());
            case Constants.PARSER_SLR:
                return new LRParser(language.getCFG(), language.getSLRParsingTable());
            case Constants.PARSER_LALR:
                return new LRParser(language.getCFG(), language.getLALRParsingTable());
            default:
                //todo 配置错误处理
                return null;
        }

    }
}
