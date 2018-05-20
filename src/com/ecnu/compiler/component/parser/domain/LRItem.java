package com.ecnu.compiler.component.parser.domain;

public class LRItem{
    //对应产生式
    private Production production;
    //点的位置
    private int pointPosition;
    //向前看符号
    private Symbol lookAhead;

    public LRItem(Production production, int pointPosition, Symbol lookAhead) {
        this.production = production;
        this.pointPosition = pointPosition;
        this.lookAhead = lookAhead;
    }

    public Production getProduction() {
        return production;
    }

    public int getPointPosition() {
        return pointPosition;
    }

    public Symbol getLookAhead() {
        return lookAhead;
    }

    public LRItem gotoNext(){
        if (pointPosition >= production.getRight().size())
            return null;
        return new LRItem(production, pointPosition + 1, lookAhead);
    }

    @Override
    public int hashCode() {
        return production.hashCode() + 31 * pointPosition + lookAhead.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){ return true; }

        if (hashCode() != obj.hashCode() || !(obj instanceof LRItem)){ return false; }

        LRItem item = (LRItem) obj;
        return production.equals(item.production) && pointPosition == item.pointPosition
                && lookAhead.equals(item.lookAhead);
    }
}
