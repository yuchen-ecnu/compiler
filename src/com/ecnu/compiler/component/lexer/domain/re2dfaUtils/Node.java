package com.ecnu.compiler.component.lexer.domain.re2dfaUtils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author @ALIREZA_KAY
 */

public class Node {

    private String symbol;
    private Node parent;
    private Node left;
    private Node right;

    private Set<Integer> firstPos;
    private Set<Integer> lastPos;
    private boolean nullable;

    public Node(String symbol) {
        this.symbol = symbol;
        parent = null;
        left = null;
        right = null;

        firstPos = new HashSet<>();
        lastPos = new HashSet<>();
        nullable = false;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void addToFirstPos(int number) {
        firstPos.add(number);
    }

    public void addAllToFirstPos(Set set) {
        firstPos.addAll(set);
    }

    public void addToLastPos(int number) {
        lastPos.add(number);
    }

    public void addAllToLastPos(Set set) {
        lastPos.addAll(set);
    }

    public Set<Integer> getFirstPos() {
        return firstPos;
    }

    public Set<Integer> getLastPos() {
        return lastPos;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
