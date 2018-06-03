package com.ecnu.compiler.component.parser.domain.ParserBuilder;

import com.ecnu.compiler.component.parser.domain.*;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;

import java.util.*;

public class LRParserBuilder {
    //构建解析表时需要的
    //CFG
    private CFG mCfg;
    //FirstFollow集
    private FirstFollowSet mFirstFollowSet;

    protected CFG getCfg() {
        return mCfg;
    }

    protected FirstFollowSet getFirstFollowSet() {
        return mFirstFollowSet;
    }

    protected LRParsingTable buildParsingTable(CFG cfg){
        //保存cfg，构建firstFollowSet
        mCfg = cfg;
        mFirstFollowSet = new FirstFollowSet(cfg);
        //创建空的ParsingTable
        List<Symbol> allSymbol = new ArrayList<>();
        allSymbol.addAll(cfg.getNonTerminalSet());
        allSymbol.addAll(cfg.getTerminalSet());
        LRParsingTable lrParsingTable = new LRParsingTable(allSymbol);
        //创建ItemSet的列表保存每个状态
        List<LRItemSet> stateList = new ArrayList<>();
        //创建Map保存状态和编号的映射
        Map<LRItemSet, Integer> stateMap = new HashMap<>();
        //添加初始状态
        LRItemSet itemSet= new LRItemSet();
        List<Symbol> productionRight = new ArrayList<>();
        productionRight.add(cfg.getAllProductions().get(0).getLeft()); //使用第一个产生式的左边作为起始符号
        Production production = new Production(new Symbol("startSymbol"), productionRight, -1); //构造初始产生式
        itemSet.add(new LRItem(production, 0, Symbol.TERMINAL_SYMBOL)); //添加初始项
        //把初始状态添加到状态列表与解析表中
        //由于初始状态只可能出现在第一次，所以就不用添加到映射中去了。
        stateList.add(getClosure(itemSet));
        lrParsingTable.addState();

        //循环构建解析表
        for (int i = 0; i < stateList.size(); i++) {
            itemSet = stateList.get(i);
            for (LRItem item : itemSet){
                //点的位置
                int pointPosition = item.getPointPosition();
                if (pointPosition == item.getProduction().getRight().size()){
                    //假如点后面没有别的符号了
                    //添加reduce表项
                    addReduceTableItem(lrParsingTable, i, item);
                } else {
                    //点后面有符号
                    //点后的符号
                    Symbol symbolAfterPoint = item.getProduction().getRight().get(pointPosition);
                    //判断是否已经计算过该符号
                    if (lrParsingTable.getItem(i, symbolAfterPoint) == null) {
                        // 表项为null，则要计算goto，并添加新状态
                        LRItemSet newItemSet = getGoto(itemSet, symbolAfterPoint);
                        Integer targetIndex = stateMap.get(newItemSet); //跳转状态编号
                        if (targetIndex == null) {
                            //假如该状态还未添加
                            stateList.add(newItemSet);
                            targetIndex = stateList.size() - 1;
                            stateMap.put(newItemSet, targetIndex);
                        }
                        //在解析表中添加新状态
                        lrParsingTable.addState();
                        //区分是否终结符
                        boolean isTerminal = cfg.getTerminalSet().contains(symbolAfterPoint);
                        //添加表项
                        lrParsingTable.set(i, symbolAfterPoint,
                                isTerminal ? LRParsingTable.SHIFT : LRParsingTable.GOTO, targetIndex);
                    }
                }
            }
        }

        //解析表最少2行，而包含s->s'的只有[1,$]项，所以设置其为acc
        lrParsingTable.set(1, Symbol.TERMINAL_SYMBOL, LRParsingTable.ACCEPT, 0);

        return lrParsingTable;
    }

    protected void addReduceTableItem(LRParsingTable lrParsingTable, int row, LRItem item){
        lrParsingTable.set(row, item.getLookAhead(), LRParsingTable.REDUCE, item.getProduction().getId());
    }

    //获得项集闭包，返回的是输入参数的引用
    protected LRItemSet getClosure(LRItemSet itemSet){
        for (LRItem item : itemSet){
            //点的位置
            int pointPosition = item.getPointPosition();
            //产生式右边
            List<Symbol> productionRight = item.getProduction().getRight();
            if (pointPosition <= productionRight.size()) {
                //点后有符号
                Symbol symbolAfterPoint = item.getProduction().getRight().get(pointPosition);
                //符号对应产生式
                List<Production> productions = mCfg.getProductions(symbolAfterPoint);
                //计算闭包后向前看符号可能的情况的列表
                //产生式点两位后的列表
                List<Symbol> beta = productionRight.subList(pointPosition + 1, productionRight.size());
                beta = new ArrayList<>(beta);
                beta.add(item.getLookAhead());
                //所求列表
                Set<Symbol> lookAheadList = mFirstFollowSet.getFirst(beta);
                //如果该符号是非终结符，则对应产生式列表不为空
                if (productions != null){
                    for (Production production : productions){
                        for (Symbol lookAhead : lookAheadList){
                            itemSet.add(new LRItem(production, 0, lookAhead));
                        }
                    }
                }
            }
        }
        return itemSet;
    }

    //求状态的跳转状态
    protected LRItemSet getGoto(LRItemSet itemSet, Symbol gotoSymbol){
        LRItemSet newItemSet = new LRItemSet();
        for (LRItem item : itemSet){
            //点的位置
            int pointPosition = item.getPointPosition();
            if (pointPosition <= item.getProduction().getRight().size()) {
                //点后有符号
                Symbol symbolAfterPoint = item.getProduction().getRight().get(pointPosition);
                if (symbolAfterPoint.equals(gotoSymbol)) {
                    //点后符号符合要求,则添加
                    newItemSet.add(item.gotoNext());
                }
            }
        }
        return getClosure(newItemSet);
    }
}
