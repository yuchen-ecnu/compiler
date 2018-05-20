package com.ecnu.compiler.component.parser.domain;

import java.util.*;
import java.util.regex.Matcher;

/**
 * 上下文无关文法
 *
 */
public class CFG {
    //非终结符映射
    private Map<Symbol, List<Integer>> mNonTerminalMap = new HashMap<>();
    //终结符集合
    private Set<Symbol> mTerminalSet = new HashSet<>();
    //产生式列表
    private List<Production> mProductions = new ArrayList<>();

    /**
     * 构造函数，需要传入CFG的列表
     */
    public CFG(String[] cfgList, Set<Symbol> mSymbolSet) {
        int nonTermId = 1;
        int prodId = 1;
        Set<String> stringSet = new HashSet<>();
        for (Symbol sym : mSymbolSet) {
            stringSet.add(sym.getType());
        }
        for (String item : cfgList) {
            item = item.trim();
            String[] result = item.split("->");
            if (result.length != 2) {
                System.err.println("格式错误，请确认输入。");
                return;
            }
            String leftStr = result[0];
            List<Integer> integerList = new ArrayList<>();
            integerList.add(0);
            Symbol leftSym = null;
            if (!stringSet.contains(leftStr)) {
                System.err.println("无法识别的左部 \"" + leftStr + "\"，请确认输入。");
                return;
            } else {
                for (Symbol sym : mSymbolSet) {
                    if (sym.getType().equals(leftStr)) {
                        leftSym = sym;
                        mTerminalSet.remove(sym);
                        sym.setTerminal(false);
                        mNonTerminalMap.put(sym, integerList);
                        break;
                    }
                }
            }
            String[] rightArr = result[1].split("\\|");
            for (String rStr : rightArr) {
                List<Symbol> rightList = new ArrayList<>();
                String[] rightStrList = rStr.trim().split(" ");
                for (String s : rightStrList) {
                    for (Symbol sym : mSymbolSet) {
                        if (sym.getType().equals(s)) {
                            rightList.add(sym);
                            if (!mNonTerminalMap.containsKey(sym)) {
                                mTerminalSet.add(sym);
                            }
                            break;
                        }
                    }
                }
//                for (int i = 1; i <= rStr.length() && !rStr.isEmpty(); i++) {
//                    String s = rStr.substring(0, i);
//                    while (stringSet.contains(s) && !rStr.isEmpty()) {
//                        for (Symbol sym : mSymbolSet) {
//                            if (sym.getType().equals(s)) {
//                                rightList.add(sym);
//                                rStr = rStr.substring(i);
//                                if (!mNonTerminalMap.containsKey(sym)) {
//                                    mTerminalSet.add(sym);
//                                }
//                                break;
//                            }
//                        }
//                        if (i <= rStr.length()) {
//                            s = rStr.substring(0, i);
//                        } else {
//                            break;
//                        }
//                    }
//                }
                Production prod = new Production(leftSym, rightList, prodId++);
                mProductions.add(prod);
            }
        }
        setListForMap(mNonTerminalMap);
    }

    public Set<Symbol> getTerminalSet(){
        return mTerminalSet;
    }

    public Set<Symbol> getNonTerminalSet(){
        return mNonTerminalMap.keySet();
    }

    public Map<Symbol, List<Integer>> getNonTerminalMap() {
        return mNonTerminalMap;
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

    //给非终结符的映射配置对应的产生式
    public void setListForMap(Map<Symbol, List<Integer>> map) {
        for (Symbol sym : map.keySet()) {
            List<Integer> intList = new ArrayList<>();
            for (Production prod : mProductions) {
                if (prod.getLeft() == sym) {
                    intList.add(prod.getId());
                }
            }
            map.put(sym, intList);
        }
    }
}
