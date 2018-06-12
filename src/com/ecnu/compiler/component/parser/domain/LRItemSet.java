package com.ecnu.compiler.component.parser.domain;

import java.util.HashSet;

public class LRItemSet extends HashSet<LRItem>{
    @Override
    public boolean add(LRItem lrItem) {
        if (!contains(lrItem)){
            return super.add(lrItem);
        }
        for (LRItem item : this){
            if (item.equals(lrItem)){
                item.addLookAheadSet(lrItem.getLookAhead());
                return false;
            }
        }
        return false;
    }
}
