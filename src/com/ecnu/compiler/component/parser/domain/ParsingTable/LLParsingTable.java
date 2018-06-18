package com.ecnu.compiler.component.parser.domain.ParsingTable;


import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.FirstFollowSet;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.*;

public class LLParsingTable extends ParsingTable {

    private Map<Symbol, List<Integer>> nonTerminalMap = new LinkedHashMap<>();
    private Set<Symbol> terminalSet = new HashSet<>();
    private Set<LLTableItem> itemSet = new HashSet<>();
    private boolean isOk = false;

    public LLParsingTable(CFG cfg) {
        nonTerminalMap = cfg.getNonTerminalMap();
        terminalSet = cfg.getTerminalSet();
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
        if (!(itemSet == null || itemSet.isEmpty())) {
            isOk = true;
        }
        if (isOk) {
            for (LLTableItem item : itemSet) {
                if (!item.isConflict()) {
                    for (LLTableItem i : itemSet) {
                        if (item.getTerm().equals(i.getTerm())
                                && item.getNonTerm().equals(i.getNonTerm())
                                && !item.getValue().equals(i.getValue())) {
                            item.setConflict(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Set<LLTableItem> getItemSet() {
        return itemSet;
    }

    public boolean isOk() {
        return isOk;
    }
}
