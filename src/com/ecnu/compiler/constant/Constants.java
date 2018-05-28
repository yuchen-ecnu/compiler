package com.ecnu.compiler.constant;
/**
 * 常量类
 * @author Michael Chen
 */
public class Constants {

    /** 空字符 */
    public static final String EMPTY_TOKEN = "";

    // RE表达式集合
    /*JAVA*/
    //标识符
    public static final String JAVA_IDENTIFIER = "^[A-Za-z_]+[A-Za-z_0-9]*$";
    //数字常量
    public static final String JAVA_CONSTANT_NUM = "^[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)([eE][+-]?[0-9]+)?$";
    //字符串常量
    public static final String JAVA_CONSTANT_STRING = "^\".*?\"$";
    //字符常量
    public static final String JAVA_CONSTANT_CHAR = "^\'[A-Za-z0-9\\*\\|\\(\\)\\\\]\'$";
    //布尔常量
    public static final String JAVA_CONSTANT_BOOL = "^(true)|(false)$";
    //关键字
    public static final String JAVA_KEY_WORD = "^(public)|(protected)|(private)|(abstract)|(class)|(interface)|(implements)|"
            + "(extends)|(new)|(static)|(final)|(const)|(native)|(synchronized)|(transient)"
            + "|(volatile)|(strictfp)|(import)|(package)|(super)|(this)|(void)|(assert)$";
    //条件句关键字
    public static final String JAVA_KEY_WORD_CONTROL =  "^(if)|(else)|(instanceof)|(switch)|(case)|(break)|(default)|(for)|(do)"
            + "|(while)|(continue)|(return)|(goto)$";
    //异常关键字
    public static final String JAVA_KEY_WORD_EXCEPTION = "^(try)|(catch)|(finally)|(throw)|(throws)$";
    //基本数据类型关键字
    public static final String JAVA_KEY_WORD_TYPE = "^(int)|(short)|(float)|(double)|(byte)"
            + "|(char)|(boolean)|(long)$";
    //保留字
    public static final String JAVA_RESERVED = "^(null)|(cast)|(future)|(generic)|(inner)|(operator)|(outer)|(rest)"
            + "|(var)|(goto)|(byValue)$";
    //注释
    public static final String JAVA_COMMENT = "^(//.*)|(/\\*(.)*?\\*/)$";
    //运算符
    public static final String JAVA_OPERATOR = "^[\\+\\-\\*/%=]$";
    //关系运算符
    public static final String JAVA_RELATIONSHIP_OPERATOR = "^(!=)|(==)|(<)|(<=)|(>)|(>=)$";
    //逻辑运算符
    public static final String JAVA_LOGIC_OPERATOR = "^(!)|(\\|\\|)|(&&)$";

    /*C++*/
    //标识符
    public static final String CPP_IDENTIFIER = "^[A-Za-z_]+[A-Za-z_0-9]*$";
    //数字常量
    public static final String CPP_CONSTANT_NUM = "^[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)([eE][+-]?[0-9]+)?$";
    //字符串常量
    public static final String CPP_CONSTANT_STRING = "^\".*?\"$";
    //字符常量
    public static final String CPP_CONSTANT_CHAR = "^\'[A-Za-z0-9\\*\\|\\(\\)\\\\]\'$";
    //布尔常量
    public static final String CPP_CONSTANT_BOOL = "^(true)|(false)$";
    //关键字
    public static final String CPP_KEY_WORD = "^(public)|(protected)|(private)|(class)|"
            + "(new)|(static)|(const)|(auto)|(delete)|(enum)|(explicit)|(export)|"
            + "(extern)|(friend)|(inline)|(namespace)|(operator)|"
            + "(register)|(struct)|(typedef)|(union)|(using)|(virtual)"
            + "|(volatile)|(this)|(void)|(assert)$";
    //条件句关键字
    public static final String CPP_KEY_WORD_CONTROL =  "^(if)|(else)|(switch)|(case)|(break)|(default)|(for)|(do)"
            + "|(while)|(continue)|(return)|(goto)$";
    //异常关键字
    public static final String CPP_KEY_WORD_EXCEPTION = "^(try)|(catch)|(throw)$";
    //基本数据类型关键字
    public static final String CPP_KEY_WORD_TYPE = "^(int)|(short)|(float)|(double)|(unsigned)"
            + "|(char)|(boolean)|(long)|(long long)|(long double)|(string)|(wchar_t)$";
    //注释
    public static final String CPP_COMMENT = "^(//.*)|(/\\*(.)*?\\*/)$";
    //运算符
    public static final String CPP_OPERATOR = "^[\\+\\-\\*/%=]$";
    //关系运算符
    public static final String CPP_RELATIONSHIP_OPERATOR = "^(!=)|(==)|(<)|(<=)|(>)|(>=)$";
    //逻辑运算符
    public static final String CPP_LOGIC_OPERATOR = "^(!)|(\\|\\|)|(&&)$";


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
    public static final int PARSER_SLR = 1;
    public static final int PARSER_LR = 2;
    public static final int PARSER_LALR = 3;




}