package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLTableItem;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

import java.util.Set;
import java.util.Stack;

/**
 * Top-Down Parsing, LL1
 *
 * @author Michael Chen
 * @date 2018-05-01 19:57
 */
public class LLParser extends Parser {

    public LLParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }

    @Override
    protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable) {
        return null;
    }

    public static void predict(String w, CFG cfg) {
//        FirstFollowSet firstFollowSet = new FirstFollowSet(cfg);
        LLParsingTable llParsingTable = new LLParsingTable(cfg);
        Set<LLTableItem> itemSet = llParsingTable.getItemSet();
        //已匹配
        Stack<Symbol> matched = new Stack<>();
        //栈
        Stack<Symbol> stack = new Stack<>();
        stack.push(Symbol.TERMINAL_SYMBOL);
        stack.push(cfg.getStartSymbol());
        //输入缓冲区
        Stack<Symbol> buffer = new Stack<>();
        buffer.push(Symbol.TERMINAL_SYMBOL);
        String[] arr = w.split(" ");
        Set<Symbol> allSymbols = cfg.getTerminalSet();
        allSymbols.addAll(cfg.getNonTerminalSet());
        for (int a = arr.length - 1; a >= 0; a--) {
            boolean found = false;
            String s = arr[a];
            for (Symbol sym : allSymbols) {
                if (sym.getType().equals(s)) {
                    buffer.push(sym);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Error: Symbol not found.");
                return;
            }
        }

        Symbol stackTop = stack.peek();
        Symbol bufferTop = buffer.peek();
        while(!stackTop.equals(Symbol.TERMINAL_SYMBOL)) {
            if (stackTop.equals(bufferTop)) {
                System.out.println("Match:" + stackTop.getType());
                matched.push(stackTop);
                stack.pop();
                buffer.pop();
            } else if (stackTop.isTerminal()) {
                System.out.println("Error: Terminal.");
                return;
            } else {
                Production prod = null;
                for (LLTableItem item : itemSet) {
                    if (item.getNonTerm().equals(stackTop) && item.getTerm().equals(bufferTop)) {
                        prod = item.getValue();
                        break;
                    }
                }
                if (prod == null) {
                    System.out.println("Error: LLTable item not found.");
                    return;
                } else {
                    System.out.print("Output:" + prod.getLeft().getType() + "->");
                    for (Symbol sym : prod.getRight()) {
                        System.out.print(sym.getType());
                    }
                    System.out.println();
                    stack.pop();
                    for (int i = prod.getRight().size() - 1; i >= 0; i--) {
                        if (!prod.getRight().get(i).equals(Symbol.EMPTY_SYMBOL)) {
                            stack.push(prod.getRight().get(i));
                        }
                    }
                }
            }
            stackTop = stack.peek();
            bufferTop = buffer.peek();
        }
    }
}
