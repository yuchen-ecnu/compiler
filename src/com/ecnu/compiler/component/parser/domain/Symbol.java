package com.ecnu.compiler.component.parser.domain;

import com.ecnu.compiler.constant.Constants;

public class Symbol {
    //终结符号
    public static final Symbol TERMINAL_SYMBOL = new Symbol(Constants.TERMINAL_TOKEN);
    //空符号
    public static final Symbol EMPTY_SYMBOL = new Symbol(Constants.EMPTY_TOKEN);
    //符号类型
    private String mName;
    //是否终结符
    private boolean mIsTerminal;

    public Symbol(String name) {
        this(name, false);
    }

    public Symbol(String name, boolean isTerminal) {
        mName = name;
        mIsTerminal = isTerminal;
    }

    public String getName() {
        return mName;
    }

    public boolean isTerminal() {
        return mIsTerminal;
    }

    public void setTerminal(boolean terminal) {
        mIsTerminal = terminal;
    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return this.mName.equals(((Symbol) obj).mName);
    }

    @Override
    public String toString() {
        return mName;
    }
}
