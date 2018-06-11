package com.ecnu.compiler.component.parser.domain.ParsingTable;

import com.ecnu.compiler.component.parser.domain.Symbol;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }
    //表
    List<TableItem[]> table;
    //表的纵坐标映射
    Map<String, Integer> colMap;

    //构造时需要传入纵坐标对应的符号集合
    public LRParsingTable(List<Symbol> symbols){
        int i = 0;
        colMap = new HashMap<>();
        table = new ArrayList<>();
        for (Symbol symbol : symbols){
            colMap.put(symbol.getType(), i);
            i++;
        }
    }

    //添加状态（横坐标）
    public void addState(){
        table.add(new TableItem[colMap.size()]);
    }

    //设置表项
    public boolean set(int state, Symbol colSymbol, char operate, int value){
        Integer col = colMap.get(colSymbol.getType());
        if (state < table.size() && col != null){
            TableItem tableItem = new TableItem();
            tableItem.operate = operate;
            tableItem.value = value;
            table.get(state)[col] = tableItem;
            return true;
        } else
            return false;
    }

    //获取表项
    public TableItem getItem(int state, Symbol colSymbol){
        Integer col = colMap.get(colSymbol.getType());
        if (state < table.size() && col != null){
            return table.get(state)[col];
        }
        return null;
    }

    public TableItem getItem(int state, String colSymbol){
        Integer col = colMap.get(colSymbol);
        if (state < table.size() && col != null){
            return table.get(state)[col];
        }
        return null;
    }
}
