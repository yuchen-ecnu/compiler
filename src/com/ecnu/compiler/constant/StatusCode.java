package com.ecnu.compiler.constant;

import java.io.Serializable;
import java.util.Date;

/**
 * 程序编译状态码
 *
 *   格式规定如下：
 *      1、长度为定长，三位，类型为int
 *      2、首位字符用于确定scope（域）
 *          1）全局            1
 *          2）状态机          2
 *          3）预处理器        3
 *          4）词法分析器      4
 *          5）语法分析器      5
 *          6）语义分析器      6
 *          7）后端执行        7
 *          8）编译器          8
 *      3、后两位编号自定义（推荐增序编号）
 *      4、强制：成功码为非负数，错误码为负数
 *
 * @author Michael Chen
 * @date 2018-05-01 15:44
 */
public enum  StatusCode implements Serializable {
    /** 全局状态码 */
    ERROR(-100,"执行出错，请查看异常信息列表"),
    SUCCESS(100,"执行成功"),

    /** 状态机 StatusCode */
    ACCEPTED(200,"token被接受"),
    REJECTED(-200,"token未被接受"),

    /** 预处理器 StatusCode */


    /** 编译器 StatusCode */
    RUNNING(800, "编译程序执行中" ),
    STAGE_INIT(801,"编译程序初始化中"),
    STAGE_PREPROCESSOR(802,"预处理器执行中"),
    STAGE_LEXER(803,"词法分析器执行中"),
    STAGE_PARSER(804,"语法分析器执行中"),
    STAGE_SEMANTIC_ANALYZER(805,"语义分析器执行中"),
    STAGE_BACKEND(806,"后端字节码处理中"),
    STAGE_FINISHED(807,"编译结束"),

    ERROR_INIT(-800,"初始化出错，请查看异常信息列表");

    /** 词法分析器 StatusCode */



    /** enum结构定义 */
    private int code;
    private String text;
    private Date time;

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    StatusCode(int code, String text, Date time) {
        this.code = code;
        this.text = text;
        this.time = time;
    }

    StatusCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "code='" + code + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
