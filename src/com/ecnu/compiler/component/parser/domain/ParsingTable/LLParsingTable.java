package com.ecnu.compiler.component.parser.domain.ParsingTable;


import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.FirstFollowSet;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LLParsingTable extends ParsingTable {

    private Set<LLTableItem> itemSet = new HashSet<>();

    public LLParsingTable(CFG cfg) {
        List<Production> productionList = cfg.getAllProductions();
        FirstFollowSet firstFollowSet = new FirstFollowSet(cfg);
        //对于FIRST(α)中的每个非终结符号a，将A→α加入到M[A, a]中
        for (Production prod : productionList) {
            Symbol left = prod.getLeft();
            List<Symbol> rightList = prod.getRight();
            Set<Symbol> firstSet = firstFollowSet.getFirst(rightList);
            for (Symbol sym : firstSet) {
                if (sym.equals(Symbol.EMPTY_SYMBOL)) {
                    Set<Symbol> followSet = firstFollowSet.getFollow(left);
                    for (Symbol s : followSet) {
//                        if (s.equals(Symbol.TERMINAL_SYMBOL)) {
//                            LLTableItem item = new LLTableItem(left, s, prod);
//                            itemSet.add(item);
//                        }
                        LLTableItem item = new LLTableItem(left, s, prod);
                        itemSet.add(item);
                    }
                } else {
                    LLTableItem item = new LLTableItem(left, sym, prod);
                    itemSet.add(item);
                }
            }
        }
    }

    public Set<LLTableItem> getItemSet() {
        return itemSet;
    }
}
