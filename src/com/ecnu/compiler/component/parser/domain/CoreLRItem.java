package com.ecnu.compiler.component.parser.domain;

public class CoreLRItem extends LRItem{

    public CoreLRItem(Production production, int pointPosition, Symbol lookAhead) {
        super(production, pointPosition, lookAhead);
    }

    public CoreLRItem(Production production, int pointPosition) {
        super(production, pointPosition);
    }

    @Override
    public int hashCode() {
        return getProduction().hashCode() + 31 * getPointPosition();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){ return true; }

        if (hashCode() != obj.hashCode() || !(obj instanceof CoreLRItem)){ return false; }

        CoreLRItem item = (CoreLRItem) obj;
        return getProduction().equals(item.getProduction()) && getPointPosition() == item.getPointPosition();
    }
}
