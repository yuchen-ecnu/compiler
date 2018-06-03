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

    public LLParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }

    @Override
    protected TD<Symbol> getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable) {
        return null;
    }

    public static TD<Symbol> predict(String w, CFG cfg) {
        //语法树
        TD<Symbol> syntaxTree = new TD<>();
        TD.TNode<Symbol> root = new TD.TNode<>();
        root.setContent(cfg.getStartSymbol());
        syntaxTree.setRoot(root);

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
                return null;
            }
        }

        Symbol stackTop = stack.peek();
        Symbol bufferTop = buffer.peek();
        while(!stackTop.equals(Symbol.TERMINAL_SYMBOL)) {
            if (stackTop.equals(bufferTop)) {
                System.out.println("Match:" + stackTop.getType());

                TD.TNode<Symbol> r = syntaxTree.getRoot();
                List<TD.TNode> children = null;
                Stack<TD.TNode<Symbol>> matchStack = new Stack<>();
                matchStack.push(r);
                while (true) {
                    if (r.getChildren().isEmpty() && !r.isMatched()) {
                        break;
                    } else {
                        matchStack.pop();
                        List<TD.TNode> myChildren = r.getChildren();
                        if (!(myChildren == null || myChildren.isEmpty())) {
                            for (int i = myChildren.size() - 1; i >= 0; i--) {
                                matchStack.push(myChildren.get(i));
                            }
                        }
                        r = matchStack.peek();
                    }
                }

                if (r.getContent().getType().equals(stackTop.getType())) {
                    r.setMatched(true);
                } else {
                    System.out.println("Tree match error!");
                }

                matched.push(stackTop);
                stack.pop();
                buffer.pop();
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

                    TD.TNode<Symbol> r = syntaxTree.getRoot();
                    List<TD.TNode> children = null;
                    Stack<TD.TNode<Symbol>> outputStack = new Stack<>();
                    outputStack.push(r);
                    while (true) {
                        if (r.getChildren().isEmpty() && !r.isMatched()) {
                            break;
                        } else {
                            outputStack.pop();
                            List<TD.TNode> myChildren = r.getChildren();
                            if (!(myChildren == null || myChildren.isEmpty())) {
                                for (int i = myChildren.size() - 1; i >= 0; i--) {
                                    outputStack.push(myChildren.get(i));
                                }
                            }
                            r = outputStack.peek();
                        }
                    }

                    if (r.getContent().getType().equals(prod.getLeft().getType())) {
                        for (Symbol s : prod.getRight()) {
                            TD.TNode<Symbol> child = new TD.TNode<>();
                            child.setContent(s);
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
                }
            }
            stackTop = stack.peek();
            bufferTop = buffer.peek();
        }
        return syntaxTree;
    }

    public static void printTree(TD<Symbol> tree) {
        Stack<TD.TNode> stack = new Stack<>();
        System.out.println("Root:" + tree.getRoot().getContent().getType());
        System.out.println("-----------");
        stack.push(tree.getRoot());
        while(!stack.isEmpty()) {
            TD.TNode<Symbol> curNode = stack.pop();
            System.out.println("Cur:" + curNode.getContent().getType());
            if (!curNode.getChildren().isEmpty()) {
                List<TD.TNode> children = curNode.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }
    }

}
