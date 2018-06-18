package com.ecnu.compiler.component.parser.domain.PredictTable;

import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.domain.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class TableEntry {
    private List<String> mItemList;

    public TableEntry(List<Symbol> symbols, Stack<Symbol> stack, Stack<Symbol> input, String action) {
        mItemList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (!(symbols == null || symbols.isEmpty())) {
            for (Symbol sym : symbols) {
                stringBuilder.append(sym.getName()).append(" ");
            }
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (Symbol sym : stack) {
            stringBuilder.append(sym.getName()).append(" ");
        }
        mItemList.add(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        for (Symbol sym : input) {
            stringBuilder.append(sym.getName()).append(" ");
        }
        mItemList.add(stringBuilder.toString());

        mItemList.add(action);
    }

    public TableEntry(Stack<Integer> stateStack, Stack<TD.TNode> treeNodeStack,
                      String input, String action, String output) {
        mItemList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> stateStackIterator = stateStack.iterator();
        Iterator<TD.TNode> treeNodeStackIterator = treeNodeStack.iterator();
        while (stateStackIterator.hasNext() && treeNodeStackIterator.hasNext()){
            stringBuilder.append(stateStackIterator.next()).append(" ").append(treeNodeStackIterator.next()).append(" ");
        }
        if (stateStackIterator.hasNext())
            stringBuilder.append(stateStackIterator.next());

        mItemList.add(stringBuilder.toString());

        mItemList.add(input);

        mItemList.add(action);

        mItemList.add(output);
    }

    public List<String> getItemList() {
        return mItemList;
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
