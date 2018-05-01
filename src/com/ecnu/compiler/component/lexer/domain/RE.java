package com.ecnu.compiler.component.lexer.domain;

/**
 * 封装正则表达式，实体类
 */
public class RE {

    private String expression;

    public RE(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
