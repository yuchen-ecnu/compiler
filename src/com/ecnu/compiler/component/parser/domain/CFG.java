package com.ecnu.compiler.component.parser.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 上下文无关文法
 *
 */
public class CFG {
    //非终结符映射
    private Map<Symbol, List<Integer>> mNonTerminalMap;
    //终结符集合
    private Set<Symbol> mTerminalSet;
    //产生式列表
    private List<Production> mProductions;

    /**
     * 构造函数，需要传入CFG的列表
     */
    public CFG(String[] cfgList){
        //todo 从String[]构造CFG
    }

    public Set<Symbol> getTerminalSet(){
        return mTerminalSet;
    }

    public Set<Symbol> getNonTerminalSet(){
        return mNonTerminalMap.keySet();
    }

    public List<Production> getAllProductions() {
        return mProductions;
    }

    /**
     * 获取某个非终结符对应的产生式
     * @param symbol 一个非终结符
     * @return 终结符对应的产生式
     */
    public List<Production> getProductions(Symbol symbol) {
        List<Integer> indexList = mNonTerminalMap.get(symbol);
        if (indexList == null)
            return null;
        List<Production> productions = new ArrayList<>();
        for (Integer index : indexList){
            productions.add(mProductions.get(index));
        }
        return productions;
    }
}
