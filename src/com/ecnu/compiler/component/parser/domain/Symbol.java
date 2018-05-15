package com.ecnu.compiler.component.parser.domain;

public class Symbol {
    //符号类型
    private String type;
    //是否终结符
    private boolean isTerminal;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }
}
