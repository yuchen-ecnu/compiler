package com.ecnu.compiler.component.parser.domain.ParsingTable;

import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.*;

public class LRParsingTable extends ParsingTable {
    //4种表项操作
    public static final char SHIFT = 's';
    public static final char REDUCE = 'r';
    public static final char ACCEPT = 'a';
    public static final char GOTO = 'g';
    //表项
    static public class TableItem{
        private char operate;
        private int value;

        public char getOperate() {
            return operate;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(operate) + String.valueOf(value);
        }
    }
    //表
    private List<TableItem[]> mTable;
    //表的纵坐标映射
    private Map<String, Integer> mColMap;
    //表的两部分划分
    private int mDivOfTowParts;

    //构造时需要传入纵坐标对应的符号集合
    public LRParsingTable(Set<Symbol> terminalSymbols, Set<Symbol> nonterminalSymbols){
        int i = 0;
        mColMap = new HashMap<>();
        mTable = new ArrayList<>();
        for (Symbol symbol : terminalSymbols){
            mColMap.put(symbol.getName(), i);
            i++;
        }
        mDivOfTowParts = i;
        for (Symbol symbol : nonterminalSymbols){
            mColMap.put(symbol.getName(), i);
            i++;
        }
    }

    public List<TableItem[]> getTable() {
        return mTable;
    }

    public Map<String, Integer> getColMap() {
        return mColMap;
    }

    public int getDivOfTowParts() {
        return mDivOfTowParts;
    }

    //添加状态（横坐标）
    public void addState(){
        mTable.add(new TableItem[mColMap.size()]);
    }

    //设置表项
    public boolean set(int state, Symbol colSymbol, char operate, int value){
        Integer col = mColMap.get(colSymbol.getName());
        if (state < mTable.size() && col != null){
            TableItem tableItem = new TableItem();
            tableItem.operate = operate;
            tableItem.value = value;
            mTable.get(state)[col] = tableItem;
            return true;
        } else
            return false;
    }

    //获取表项
    public TableItem getItem(int state, Symbol colSymbol){
        Integer col = mColMap.get(colSymbol.getName());
        if (state < mTable.size() && col != null){
            return mTable.get(state)[col];
        }
        return null;
    }

    public TableItem getItem(int state, String colSymbol){
        Integer col = mColMap.get(colSymbol);
        if (state < mTable.size() && col != null){
            return mTable.get(state)[col];
        }
        return null;
    }

}
