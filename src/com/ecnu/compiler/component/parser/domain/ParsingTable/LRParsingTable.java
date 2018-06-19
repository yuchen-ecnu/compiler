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
        protected char mOperate;
        protected int mValue;

        public char getOperate() {
            return mOperate;
        }

        public int getValue() {
            return mValue;
        }

        @Override
        public String toString() {
            return String.valueOf(mOperate) + String.valueOf(mValue);
        }

        public String getStringItem(){
            return toString();
        }
    }

    static public class ErrorTableItem extends TableItem{
        private List<TableItem> mErrorItemList;



        public ErrorTableItem(TableItem oldTableItem) {
            mOperate = oldTableItem.getOperate();
            mValue = oldTableItem.getValue();
            mErrorItemList = new ArrayList<>();
        }

        private void addErrorTableItem(TableItem tableItem){
            mErrorItemList.add(tableItem);
        }

        private void addErrorTableItem(char operate, int value){
            TableItem tableItem = new TableItem();
            tableItem.mOperate = operate;
            tableItem.mValue = value;
            addErrorTableItem(tableItem);
        }

        @Override
        public String getStringItem() {
            return toString();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(super.toString());
            for (TableItem tableItem : mErrorItemList){
                stringBuilder.append(" | ").append(tableItem.toString());
            }
            return stringBuilder.toString();
        }
    }
    //表
    private List<TableItem[]> mTable;
    //表的纵坐标映射
    private Map<String, Integer> mColMap;
    //表的两部分划分
    private int mDivOfTowParts;

    public LRParsingTable() { }

    //构造时需要传入纵坐标对应的符号集合
    public LRParsingTable(Set<Symbol> terminalSymbols, Set<Symbol> nonterminalSymbols){
        initLRParsingTable(terminalSymbols, nonterminalSymbols);
    }

    public void initLRParsingTable(Set<Symbol> terminalSymbols, Set<Symbol> nonterminalSymbols){
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
        boolean result = false;
        Integer col = mColMap.get(colSymbol.getName());
        if (state < mTable.size() && col != null){
            TableItem oldTableItem = mTable.get(state)[col];
            if (oldTableItem == null){
                TableItem tableItem = new TableItem();
                tableItem.mOperate = operate;
                tableItem.mValue = value;
                mTable.get(state)[col] = tableItem;
                result = true;
            } else if (oldTableItem.getClass() == TableItem.class){
                ErrorTableItem errorTableItem = new ErrorTableItem(oldTableItem);
                errorTableItem.addErrorTableItem(operate, value);
                mTable.get(state)[col] = errorTableItem;
            } else if (oldTableItem.getClass() == ErrorTableItem.class){
                ErrorTableItem errorTableItem = (ErrorTableItem) oldTableItem;
                errorTableItem.addErrorTableItem(operate, value);
            }
        }
        return result;
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
