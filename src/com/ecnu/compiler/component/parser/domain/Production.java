package com.ecnu.compiler.component.parser.domain;

import java.util.List;

/**
 * 产生式
 */
public class Production {
    //编号
    private int id = -1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return right.hashCode() + 31 * left.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }

        if (hashCode() != obj.hashCode() || !(obj instanceof Production)){ return false; }

        Production p = (Production) obj;
        return right.equals(p.right) && left.equals(p.left);
    }
}
