package com.ecnu.compiler.component.parser.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirstFollowSet {
    //First集映射
    private Map<Symbol, Set<Symbol>> firstMap;
    //Follow集映射
    private Map<Symbol, Set<Symbol>> followMap;

    public FirstFollowSet(CFG cfg) {
        //todo 根据cfg构造FirstFollowSet，这里要考虑结束符号$
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

}
