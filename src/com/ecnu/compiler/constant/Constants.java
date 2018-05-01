package com.ecnu.compiler.constant;

/**
 * 常量类
 * @author Michael Chen
 */
public class Constants {

    /** 以下为示例 */
    public static final String EXAMPLE = "example";

    /**
     * RE表达式集合
     * @author ZhaoChen
     */
    //标识符
    public static final String JAVA_IDENTIFIER = "^[A-Za-z_]+[A-Za-z_0-9]*$";
    //常量,常数
    public static final String CONSTANT = "example";
    //保留字
    public static final String RESERVED = "example";
    //注释（多行注释、反斜杠注释）
    public static final String JAVA_COMMENT = "^(//.*?\\n)|(/\\*(.|\\n)*?\\*/)$";
    //运算符
    public static final String JAVA_OPERATOR = "^[\\+\\-\\*/%]$";
    //Testtest
}
