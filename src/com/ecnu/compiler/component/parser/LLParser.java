package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLTableItem;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.TableEntry;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Top-Down Parsing, LL1
 *
 * @author Michael Chen
 * @date 2018-05-01 19:57
 */
public class LLParser extends Parser {

    private static PredictTable predictTable;

    public LLParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }

    @Override
    protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable, PredictTable predictTable) {
        return null;
    }

    public static TD predict(String w, CFG cfg) {
        //预测分析表
        predictTable = new PredictTable();
        List<TableEntry> tableEntryList = new ArrayList<>();
        //语法树
        TD.TNode<String> root = new TD.TNode<>();
        root.setContent(cfg.getStartSymbol().getType());
        TD syntaxTree = new TD(root);

        LLParsingTable llParsingTable = new LLParsingTable(cfg);
        Set<LLTableItem> itemSet = llParsingTable.getItemSet();
        //已匹配
        List<Symbol> matched = new ArrayList<>();
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
                return null;
            }
        }
//        Stack<Symbol> tempStack = stack.clone();
        TableEntry tableEntry = new TableEntry(null, stack, buffer, "");
        tableEntryList.add(tableEntry);

        Symbol stackTop = stack.peek();
        Symbol bufferTop = buffer.peek();
        while(!stackTop.equals(Symbol.TERMINAL_SYMBOL)) {
            if (stackTop.equals(bufferTop)) {
                System.out.println("Match:" + stackTop.getType());

                TD.TNode<String> r = syntaxTree.getRoot();
                List<TD.TNode> children = null;
                Stack<TD.TNode<String>> matchStack = new Stack<>();
                matchStack.push(r);
                while (true) {
                    if (r.getChildren().isEmpty() && !r.isMatched()) {
                        break;
                    } else {
                        matchStack.pop();
                        List<TD.TNode<String>> myChildren = r.getChildren();
                        if (!(myChildren == null || myChildren.isEmpty())) {
                            for (int i = myChildren.size() - 1; i >= 0; i--) {
                                matchStack.push(myChildren.get(i));
                            }
                        }
                        r = matchStack.peek();
                    }
                }

                if (r.getContent().equals(stackTop.getType())) {
                    r.setMatched(true);
                } else {
                    System.out.println("Tree match error!");
                }

                matched.add(stackTop);
                stack.pop();
                buffer.pop();

                TableEntry entry = new TableEntry(matched, stack, buffer, "Match:" + stackTop.getType());
                tableEntryList.add(entry);
            } else if (stackTop.isTerminal()) {
                System.out.println("Error: Terminal.");
                return null;
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
                    return null;
                } else {
                    System.out.print("Output:" + prod.getLeft().getType() + "->");
                    for (Symbol sym : prod.getRight()) {
                        System.out.print(sym.getType());
                    }
                    System.out.println();

                    TD.TNode<String> r = syntaxTree.getRoot();
                    List<TD.TNode> children = null;
                    Stack<TD.TNode<String>> outputStack = new Stack<>();
                    outputStack.push(r);
                    while (true) {
                        if (r.getChildren().isEmpty() && !r.isMatched()) {
                            break;
                        } else {
                            outputStack.pop();
                            List<TD.TNode<String>> myChildren = r.getChildren();
                            if (!(myChildren == null || myChildren.isEmpty())) {
                                for (int i = myChildren.size() - 1; i >= 0; i--) {
                                    outputStack.push(myChildren.get(i));
                                }
                            }
                            r = outputStack.peek();
                        }
                    }

                    if (r.getContent().equals(prod.getLeft().getType())) {
                        for (Symbol s : prod.getRight()) {
                            TD.TNode<String> child = new TD.TNode<>();
                            child.setContent(s.getType());
                            if (s.equals(Symbol.EMPTY_SYMBOL)) {
                                child.setMatched(true);
                            }
                            r.addChild(child);
                        }
                    } else {
                        System.out.println("Tree output error!");
                    }

                    stack.pop();
                    for (int i = prod.getRight().size() - 1; i >= 0; i--) {
                        if (!prod.getRight().get(i).equals(Symbol.EMPTY_SYMBOL)) {
                            stack.push(prod.getRight().get(i));
                        }
                    }

                    String output = "Output:" + prod.getLeft().getType() + "->";
                    for (Symbol sym : prod.getRight()) {
                        output += sym.getType() + " ";
                    }
                    TableEntry entry = new TableEntry(matched, stack, buffer, output);
                    tableEntryList.add(entry);
                }
            }
            stackTop = stack.peek();
            bufferTop = buffer.peek();
        }
        predictTable.setTableEntryList(tableEntryList);
        return syntaxTree;
    }

    public static void printTree(TD tree) {
        Stack<TD.TNode> stack = new Stack<>();
        System.out.println("Root:" + tree.getRoot().getContent());
        System.out.println("-----------");
        stack.push(tree.getRoot());
        while(!stack.isEmpty()) {
            TD.TNode<String> curNode = stack.pop();
            System.out.println("Cur:" + curNode.getContent());
            if (!curNode.getChildren().isEmpty()) {
                List<TD.TNode<String>> children = curNode.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }
    }

    public static PredictTable getPredictTable() {
        return predictTable;
    }
}
