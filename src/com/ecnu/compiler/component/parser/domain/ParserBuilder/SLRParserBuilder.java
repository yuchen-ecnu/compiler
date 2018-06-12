package com.ecnu.compiler.component.parser.domain.ParserBuilder;

import com.ecnu.compiler.component.parser.domain.LRItem;
import com.ecnu.compiler.component.parser.domain.LRItemSet;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SLRParserBuilder extends LRParserBuilder {

    @Override
    protected LRItemSet getClosure(LRItemSet itemSet) {
        ArrayList<LRItem> lrItemList = new ArrayList<>(itemSet);
        for (int i = 0; i < lrItemList.size(); i++) {
            LRItem item = lrItemList.get(i);
            //点的位置
            int pointPosition = item.getPointPosition();
            //产生式右边
            List<Symbol> productionRight = item.getProduction().getRight();
            if (pointPosition < productionRight.size()) {
                //点后有符号
                Symbol symbolAfterPoint = item.getProduction().getRight().get(pointPosition);
                //符号对应产生式
                List<Production> productions = getCfg().getProductions(symbolAfterPoint);
                //如果该符号是非终结符，则对应产生式列表不为空
                if (productions != null){
                    for (Production production : productions){
                        itemSet.add(new LRItem(production, 0, Symbol.TERMINAL_SYMBOL));
                    }
                }
            }
        }
        return itemSet;
    }

    @Override
    protected void addReduceTableItem(LRParsingTable lrParsingTable, int row, LRItem item) {
        Set<Symbol> followSymbols = getFirstFollowSet().getFollow(item.getProduction().getLeft());
        for (Symbol symbol : followSymbols){
            lrParsingTable.set(row, symbol, LRParsingTable.REDUCE, item.getProduction().getId() - 1);
        }
    }
}
