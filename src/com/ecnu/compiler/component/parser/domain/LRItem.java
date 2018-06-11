package com.ecnu.compiler.component.parser.domain;

import java.util.HashSet;
import java.util.Set;

public class LRItem{
    //对应产生式
    private Production mProduction;
    //点的位置
    private int mPointPosition;
    //向前看符号
    private Set<Symbol> mLookAheadSet;

    public LRItem(Production production, int pointPosition, Symbol lookAhead) {
        this.mProduction = production;
        this.mPointPosition = pointPosition;
        mLookAheadSet = new HashSet<>();
        mLookAheadSet.add(lookAhead);
    }


    public LRItem(Production production, int pointPosition) {
        this.mProduction = production;
        this.mPointPosition = pointPosition;
        mLookAheadSet = new HashSet<>();
    }


    public Production getProduction() {
        return mProduction;
    }

    public int getPointPosition() {
        return mPointPosition;
    }

    public Set<Symbol> getLookAhead() {
        return mLookAheadSet;
    }

    public void addLookAhead(Symbol lookAhead){
        mLookAheadSet.add(lookAhead);
    }

    public void addLookAheadSet(Set<Symbol> lookAheadSet){
        mLookAheadSet.addAll(lookAheadSet);
    }

    public LRItem gotoNext(){
        if (mPointPosition >= mProduction.getRight().size())
            return null;
        LRItem newItem = new LRItem(mProduction, mPointPosition + 1);
        newItem.addLookAheadSet(mLookAheadSet);
        return newItem;
    }

    @Override
    public int hashCode() {
        return mProduction.hashCode() + 31 * mPointPosition + mLookAheadSet.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){ return true; }

        if (hashCode() != obj.hashCode() || !(obj instanceof LRItem)){ return false; }

        LRItem item = (LRItem) obj;
        return mProduction.equals(item.mProduction) && mPointPosition == item.mPointPosition
                && mLookAheadSet.equals(item.mLookAheadSet);
    }
}
