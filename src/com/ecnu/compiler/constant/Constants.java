package com.ecnu.compiler.constant;

/**
 * 常量类
 * @author Michael Chen
 */
public class Constants {

    /** 空字符 */
    public static final String EMPTY_TOKEN = "";

    // RE表达式集合
    /**标识符*/
    public static final String JAVA_IDENTIFIER = "^[A-Za-z_]+[A-Za-z_0-9]*$";
    /**常量,常数*/
    public static final String JAVA_CONSTANT = "example";
    /**保留字*/
    public static final String JAVA_RESERVED = "example";
    /**注释（多行注释、反斜杠注释）*/
    public static final String JAVA_COMMENT = "^(//.*?\\n)|(/\\*(.|\\n)*?\\*/)$";
    /**运算符*/
    public static final String JAVA_OPERATOR = "^[\\+\\-\\*/%]$";

    /** 无关字符（注释、换行、回车、制表符） */
    public static final String IRRELEVANT_TOKEN = "";


    /** 配置文件常量 */
    //支持语种
    public static final String LANGUAGE_JAVA = "java";
    public static final String LANGUAGE_CPLUS = "c++";
    public static final String LANGUAGE_C = "c";

    //执行策略(数值越大精细化程度越高)
    public static final int EXECUTE_IN_ONE_STEP = 0;
    public static final int EXECUTE_STAGE_BY_STAGE = 1;
    public static final int EXECUTE_STEP_BY_STEP = 2;

    //词法分析器执行算法
    public static final int RE_TO_DFA = 0;
    public static final int RE_NFA_DFA = 1;

    //语法分析器执行算法
    public static final int PARSER_LL = 0;
    public static final int PARSER_LR = 1;




}
