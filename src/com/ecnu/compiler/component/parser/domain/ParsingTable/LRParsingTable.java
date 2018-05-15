package com.ecnu.compiler.component.parser.domain.ParsingTable;

import com.ecnu.compiler.component.parser.domain.Symbol;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class LRParsingTable extends ParsingTable {
    //4种表项操作
    public static final char SHIFT = 's';
    public static final char REDUCE = 'r';
    public static final char ACCEPT = 'a';
    public static final char GOTO = 'g';
    //表项
    private class TableItem{
        char operate;
        int value;
    }
    //表
    List<TableItem[]> table;
    //表的纵坐标映射
    Map<Symbol, Integer> colMap;

    //构造时需要传入纵坐标对应的符号集合
    public LRParsingTable(List<Symbol> symbols){
        //todo 构造符号映射
    }

    //添加状态（横坐标）
    public void addState(){
        table.add(new TableItem[colMap.size()]);
    }

    //设置表项
    public boolean set(int state, Symbol colSymbol, char operate, int value){
        Integer col = colMap.get(colSymbol);
        if (state < table.size() && col != null && table.get(state)[col] != null){
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
        Integer col = colMap.get(colSymbol);
        if (state < table.size() && col != null){
            return table.get(state)[col];
        }
        return null;
    }
}
