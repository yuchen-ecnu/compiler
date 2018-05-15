package com.ecnu.compiler.component.parser.domain;

import java.util.List;

/**
 * 产生式
 */
public class Production {
    //产生式左边
    private Symbol left;
    //产生式右边
    private List<Symbol> right;

    public Symbol getLeft() {
        return left;
    }

    public void setLeft(Symbol left) {
        this.left = left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public void setRight(List<Symbol> right) {
        this.right = right;
    }
}
