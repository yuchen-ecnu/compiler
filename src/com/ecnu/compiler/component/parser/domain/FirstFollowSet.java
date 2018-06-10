package com.ecnu.compiler.component.parser.domain;

import java.util.*;

public class FirstFollowSet {
    //First集映射
    private Map<Symbol, Set<Symbol>> firstMap = new HashMap<>();
    //Follow集映射
    private Map<Symbol, Set<Symbol>> followMap = new HashMap<>();

    public FirstFollowSet(CFG cfg) {
        Symbol endSym = Symbol.TERMINAL_SYMBOL;
        cfg.getTerminalSet().add(endSym);
        calculateFirst(cfg);
        calculateFollow(cfg);
    }

    private void calculateFirst(CFG cfg) {
        Map<Symbol, List<Integer>> nonTerminalMap = cfg.getNonTerminalMap();
        Set<Symbol> terminalSet = cfg.getTerminalSet();
        List<Production> productionList = cfg.getAllProductions();
        //若X是终结符号，FIRST(X) = {X}
        for (Symbol termSym : terminalSet) {
            Set<Symbol> symbolSet = new HashSet<>();
            symbolSet.add(termSym);
            firstMap.put(termSym, symbolSet);
        }
        //若存在产生式X->ε，把ε加入FIRST(X)中
        for (Production prod : productionList) {
            if (prod.getRight().size() == 1 && prod.getRight().get(0) == Symbol.EMPTY_SYMBOL) {
                Symbol leftSym = prod.getLeft();
                Set<Symbol> symbolSet = firstMap.get(leftSym);
                symbolSet.add(Symbol.EMPTY_SYMBOL);
                firstMap.put(leftSym, symbolSet);
            }
        }
        //若X是非终结符号
        boolean changed = true;
        while(changed) {
            changed = false;
            for (Symbol nonTermSym : nonTerminalMap.keySet()) {
                Set<Symbol> symbolSet = firstMap.get(nonTermSym);
                if (symbolSet == null) {
                    symbolSet = new HashSet<>();
                }
                for (Production prod : productionList) {
                    if (prod.getLeft() == nonTermSym) {
                        boolean flag = true; //是否遍历过的FIRST(Y)包含ε
                        for (Symbol sym : prod.getRight()) {
                            if (getFirst(sym) != null) {
                                int size = symbolSet.size();
                                symbolSet.addAll(getFirst(sym));
                                if (size != symbolSet.size()) {
                                    changed = true;
                                }
                            }
                            if (!(getFirst(sym) != null && getFirst(sym).contains(Symbol.EMPTY_SYMBOL))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            int size = symbolSet.size();
                            symbolSet.add(Symbol.EMPTY_SYMBOL);
                            if (size != symbolSet.size()) {
                                changed = true;
                            }
                        }
                    }
                }
                firstMap.put(nonTermSym, symbolSet);
            }
        }
    }

    private void calculateFollow(CFG cfg) {
        Symbol termSym = Symbol.TERMINAL_SYMBOL;
        //若B是开始符号，把$加入FOLLOW(B)中
        Symbol startSym = cfg.getStartSymbol();
        Set<Symbol> startSymbolSet = new HashSet<>();
        startSymbolSet.add(termSym);
        followMap.put(startSym, startSymbolSet);
        //遍历产生式
        boolean changed = true;
        while(changed) {
            changed = false;
            for (Production prod : cfg.getAllProductions()) {
                Symbol left = prod.getLeft();
                List<Symbol> rightList = prod.getRight();
                for (Symbol sym : cfg.getNonTerminalSet()) {
                    Set<Symbol> symbolSet = getFollow(sym);
                    if (symbolSet == null) {
                        symbolSet = new HashSet<>();
                    }
                    int size = symbolSet.size();
                    List<Symbol> alphaList = new ArrayList<>();
                    List<Symbol> betaList = new ArrayList<>();
                    boolean found = false; //是否已经遍历到了B
                    for (Symbol s : rightList) {
                        if (!found) {
                            if (s != sym) {
                                alphaList.add(s);
                            } else {
                                found = true;
                            }
                        } else {
                            if (s != sym) {
                                betaList.add(s);
                            }
                        }
                    }
                    //处理产生式A->αB
                    if (!found) {
                        continue;
                    }
                    if (betaList.isEmpty()) {
                        symbolSet.addAll(getFollow(left));
                        if (size != symbolSet.size()) {
                            changed = true;
                        }
                    }
                    //处理产生式A->αBβ
                    else {
                        symbolSet.addAll(getFirst(betaList));
                        if (getFirst(betaList).contains(Symbol.EMPTY_SYMBOL)) {
                            symbolSet.remove(Symbol.EMPTY_SYMBOL);
                            symbolSet.addAll(getFollow(left));
                        }
                        if (size != symbolSet.size()) {
                            changed = true;
                        }
                    }
                    followMap.put(sym, symbolSet);
                }
            }
        }
    }

    public Set<Symbol> getFirst(Symbol symbol){
        return firstMap.get(symbol);
    }

    public Set<Symbol> getFollow(Symbol symbol){
        return followMap.get(symbol);
    }

    public Set<Symbol> getFirst(List<Symbol> symbols){
        //结果集合
        Set<Symbol> firstSet = new HashSet<>();
        for (Symbol symbol : symbols){
            //当前符号对应的first集，将其添加到结果中
            Set<Symbol> tmpSymbolSet = firstMap.get(symbol);
            firstSet.addAll(tmpSymbolSet);
            //如果发现当前first集中没有空符号，则删除结果中的空符号，然后返回结果，否则继续查看下一个符号的first集合
            if (!tmpSymbolSet.contains(Symbol.EMPTY_SYMBOL)){
                firstSet.remove(Symbol.EMPTY_SYMBOL);
                return firstSet;
            }
        }
        return firstSet;
    }

    //获得对应符号列表的follow集合
    public Set<Symbol> getFollow(List<Symbol> symbols){
        return getFollow(symbols.get(symbols.size() - 1));
    }

    public Map<Symbol, Set<Symbol>> getFirstMap() {
        return firstMap;
    }

    public Map<Symbol, Set<Symbol>> getFollowMap() {
        return followMap;
    }
}
