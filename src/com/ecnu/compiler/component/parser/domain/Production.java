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
    //编号
    private int id;

    public Production(Symbol left, List<Symbol> right, int id) {
        this.left = left;
        this.right = right;
        this.id = id;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
