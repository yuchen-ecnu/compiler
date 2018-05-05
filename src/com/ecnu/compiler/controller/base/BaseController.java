package com.ecnu.compiler.controller.base;

import com.ecnu.compiler.component.lexer.DirectLexer;
import com.ecnu.compiler.component.lexer.IndirectLexer;
import com.ecnu.compiler.component.lexer.base.BaseLexer;
import com.ecnu.compiler.component.parser.LLParser;
import com.ecnu.compiler.component.parser.LRParser;
import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.component.semantic.SemanticAnalyzer;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;

import java.io.File;

/**
 * 控制器 基类（控制器行为集合）
 * @author Michael Chen
 */
public abstract class BaseController {

    //存储
    /** 待处理文件 */
    protected File file;
    /** 异常列表 */
    protected ErrorList errorList;

    /** 编译状态码 */
    protected StatusCode status;
    /** 配置参数 */
    protected Config config;

    /** 预处理器 */
    protected Preprocessor preprocessor;
    /** 词法分析器 */
    protected BaseLexer lexer;
    /** 语法分析器 */
    protected Parser parser;
    /** 语义分析器 */
    protected SemanticAnalyzer semanticAnalyzer;
    /** Backend处理器 */

    public BaseController(File file, Config config) {
        this.config = config;
        this.file = file;
        this.errorList = new ErrorList();
        this.lexer = createLexer();
        this.parser = createParser();
        this.status = StatusCode.STAGE_INIT;
    }

    /** 执行下一步程序 */
    public abstract StatusCode next();

    /**
     * 创建词法分析器
     * @return 对应算法的词法分析器
     */
    private BaseLexer createLexer() {
        switch(config.getLexerAlgorithm()){
            case Constants.RE_NFA_DFA:
                return new IndirectLexer(file);
            case Constants.RE_TO_DFA:
                return new DirectLexer(file);
            default:
                return null;
        }
    }

    /**
     * 创建语法分析器
     * @return 对应算法的词法分析器
     */
    private Parser createParser() {
        switch(config.getParserAlgorithm()){
            case Constants.PARSER_LL:
                return new LLParser();
            case Constants.PARSER_LR:
                return new LRParser();
            default:
                return null;
        }

    }
}
