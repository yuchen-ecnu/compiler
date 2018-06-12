package com.ecnu.compiler.component.parser.domain.PredictTable;

import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.List;
import java.util.Stack;

public class TableEntry {
    private String symbols; //LL中的"已匹配",LR中的"符号"
    private String stack;
    private String input;
    private String action;

    public TableEntry(List<Symbol> symbols, Stack<Symbol> stack, Stack<Symbol> input, String action) {
        this.action = action;
        StringBuilder stringBuilder = new StringBuilder();
        if (!(symbols == null || symbols.isEmpty())) {
            for (Symbol sym : symbols) {
                stringBuilder.append(sym.getType()).append(" ");
            }
        }
        this.symbols = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        for (Symbol sym : stack) {
            stringBuilder.append(sym.getType()).append(" ");
        }
        this.stack = stringBuilder.toString();
        for (Symbol sym : input) {
            stringBuilder.append(sym.getType()).append(" ");
        }
        this.input = stringBuilder.toString();
    }



    @Override
    public String toString() {
        String symbols = "Symbols: " + this.symbols;
        String stack = "Stack: " + this.stack;
        String input = "Input: " + this.input;
        String action = "Action: " + this.action;
        return symbols + "\n" + stack + "\n" + input + "\n" + action + "\n";
    }
}
