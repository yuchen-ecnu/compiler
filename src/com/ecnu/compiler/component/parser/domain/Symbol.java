package com.ecnu.compiler.component.parser.domain;

public class Symbol {
    //终结符号
    public static final Symbol TERMINAL_SYMBOL = new Symbol("$");
    //空符号
    public static final Symbol EMPTY_SYMBOL = new Symbol("epsilon");
    //符号类型
    private String type;
    //是否终结符
    private boolean isTerminal;

    public Symbol(String type) {
        this(type, false);
    }

    public Symbol(String type, boolean isTerminal) {
        this.type = type;
        this.isTerminal = isTerminal;
    }

    public String getType() {
        return type;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return type.equals(obj);
    }
}
