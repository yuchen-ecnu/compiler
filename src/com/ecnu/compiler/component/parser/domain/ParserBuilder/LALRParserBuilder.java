package com.ecnu.compiler.component.parser.domain.ParserBuilder;

import com.ecnu.compiler.component.parser.domain.*;

import java.util.Set;

public class LALRParserBuilder extends LRParserBuilder {
    @Override
    protected LRItem getNewLRItem(Production production, Symbol lookahead) {
        return new CoreLRItem(production, 0, lookahead);
    }

    @Override
    protected LRItem getNewLRItem(Production production, Set<Symbol> lookaheadSet) {
        LRItem lrItem = new CoreLRItem(production, 0);
        lrItem.addLookAheadSet(lookaheadSet);
        return lrItem;
    }
}
