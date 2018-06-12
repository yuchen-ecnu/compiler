package com.ecnu.compiler.component.parser.domain.PredictTable;

import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TableEntry {
    private List<String> mItemList;

    public TableEntry(List<Symbol> symbols, Stack<Symbol> stack, Stack<Symbol> input, String action) {
        mItemList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (!(symbols == null || symbols.isEmpty())) {
            for (Symbol sym : symbols) {
                stringBuilder.append(sym.getType()).append(" ");
            }
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (Symbol sym : stack) {
            stringBuilder.append(sym.getType()).append(" ");
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (Symbol sym : input) {
            stringBuilder.append(sym.getType()).append(" ");
        }
        mItemList.add(stringBuilder.toString());

        mItemList.add(action);
    }

    public TableEntry(Stack<Integer> stateStack, Stack<TD.TNode<String>> treeNodeStack,
                      List<Token> tokenList, int curIndex, String action) {
        mItemList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int state : stateStack){
            stringBuilder.append(state);
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (TD.TNode<String> node : treeNodeStack){
            stringBuilder.append(node.getContent());
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (int i = curIndex; i < tokenList.size(); i++) {
            stringBuilder.append(tokenList.get(i).getType());
        }
        mItemList.add(stringBuilder.toString());

        mItemList.add(action);
    }

    @Override
    public String toString() {
        String symbols = "Symbols: " + mItemList.get(0);
        String stack = "Stack: " + mItemList.get(1);
        String input = "Input: " + mItemList.get(2);
        String action = "Action: " + mItemList.get(3);
        return symbols + "\n" + stack + "\n" + input + "\n" + action + "\n";
    }
}
