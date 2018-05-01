package com.ecnu.compiler.constant;

/**
 * 配置常量
 *
 * @author Michael Chen
 * @date 2018-05-01 15:39
 */
public class Config {
    /** 目标编译语言 (默认C++) */
    private String COMPILE_LANGUAGE = Constants.LANGUAGE_CPLUS;
    /** 编译器执行方式 (默认完整执行) */
    private int EXECUTE_TYPE = Constants.EXECUTE_IN_ONE_STEP;
    /** 词法分析器执行算法 (默认RE->DFA) */
    private int LEXER_ALGORITHM = Constants.RE_TO_DFA;
    /** 语法分析器执行算法 (默认LLParser) */
    private int PARSER_ALGORITHM = Constants.PARSER_LL;

    public Config() {
    }

    public String getCompileLanguage() {
        return COMPILE_LANGUAGE;
    }

    public void setCompileLanguage(String compileLanguage) {
        this.COMPILE_LANGUAGE = compileLanguage;
    }

    public int getExecuteType() {
        return EXECUTE_TYPE;
    }

    public void setExecuteType(int executeType) {
        this.EXECUTE_TYPE = executeType;
    }

    public int getLexerAlgorithm() {
        return LEXER_ALGORITHM;
    }

    public void setLexerAlgorithm(int lexerAlgorithm) {
        this.LEXER_ALGORITHM = lexerAlgorithm;
    }

    public int getParserAlgorithm() {
        return PARSER_ALGORITHM;
    }

    public void setParserAlgorithm(int parserAlgorithm) {
        this.PARSER_ALGORITHM = parserAlgorithm;
    }
}
