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
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.StatusCode;

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

//    private static PredictTable predictTable;

    public LLParser(CFG CFG, ParsingTable parsingTable, ErrorList errorList) {
        super(CFG, parsingTable, errorList);
    }

    @Override
    protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable, PredictTable predictTable) {
        //添加预测表表头
        ArrayList<String> tableHead = new ArrayList<>();
        tableHead.add("Matched");
        tableHead.add("Stack");
        tableHead.add("Input");
        tableHead.add("Action");
        predictTable.setTableHead(tableHead);
        List<TableEntry> tableEntryList = new ArrayList<>();
        //语法树
        TD.TNode root = new TD.TNode();
        root.setContent(cfg.getStartSymbol().getName());
        TD syntaxTree = new TD(root);

        LLParsingTable llParsingTable = (LLParsingTable) parsingTable;
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

        Set<Symbol> allSymbols = cfg.getTerminalSet();
        allSymbols.addAll(cfg.getNonTerminalSet());
        List<Token> tokens = symbolTable.getTokens();
        for (int i = tokens.size() - 1; i >= 0; i--) {
            Token t = tokens.get(i);
            boolean found = false;
            String s = t.getType();
            for (Symbol sym : allSymbols) {
                if (sym.getName().equals(s)) {
                    buffer.push(sym);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Error: Symbol not found.");
                ErrorMsg errorMsg = new ErrorMsg(400, "语法分析错误，无法识别的符号" + s, StatusCode.ERROR_PARSER);
                getErrorList().addError(errorMsg);
                return null;
            }
        }
        TableEntry tableEntry = new TableEntry(null, stack, buffer, "");
        tableEntryList.add(tableEntry);

        Symbol stackTop = stack.peek();
        Symbol bufferTop = buffer.peek();
        while(!stackTop.equals(Symbol.TERMINAL_SYMBOL)) {
            if (stackTop.equals(bufferTop)) {
                System.out.println("Match:" + stackTop.getName());

                TD.TNode r = syntaxTree.getRoot();
                List<TD.TNode> children = null;
                Stack<TD.TNode> matchStack = new Stack<>();
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

                if (r.getContent().equals(stackTop.getName())) {
                    r.setMatched(true);
                } else {
                    System.out.println("Tree match error!");
                    ErrorMsg errorMsg = new ErrorMsg(401, "语法分析错误，栈顶符号错误，应为" + stackTop.getName() + "，现为" + r.getContent(), StatusCode.ERROR_PARSER);
                    getErrorList().addError(errorMsg);
                    return null;
                }

                matched.add(stackTop);
                stack.pop();
                buffer.pop();

                TableEntry entry = new TableEntry(matched, stack, buffer, "Match:" + stackTop.getName());
                tableEntryList.add(entry);
            } else if (stackTop.isTerminal()) {
                System.out.println("Error: Terminal.");
                ErrorMsg errorMsg = new ErrorMsg(402, "语法分析错误，栈顶出现终结符" + stackTop.getName(), StatusCode.ERROR_PARSER);
                getErrorList().addError(errorMsg);
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
                    ErrorMsg errorMsg = new ErrorMsg(403, "语法分析错误，查表失败，找不到[" + stackTop.getName() + ", " + bufferTop.getName() + "]", StatusCode.ERROR_PARSER);
                    getErrorList().addError(errorMsg);
                    return null;
                } else {
                    System.out.print("Output:" + prod.getLeft().getName() + "->");
                    for (Symbol sym : prod.getRight()) {
                        System.out.print(sym.getName());
                    }
                    System.out.println();

                    TD.TNode r = syntaxTree.getRoot();
                    List<TD.TNode> children = null;
                    Stack<TD.TNode> outputStack = new Stack<>();
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

                    if (r.getContent().equals(prod.getLeft().getName())) {
                        for (Symbol s : prod.getRight()) {
                            TD.TNode child = new TD.TNode();
                            child.setContent(s.getName());
                            if (s.equals(Symbol.EMPTY_SYMBOL)) {
                                child.setMatched(true);
                            }
                            r.addChild(child);
                            if (r.getProductionId() <= 0) {
                                r.setProductionId(prod.getId());
                            }
                        }
                    } else {
                        System.out.println("Tree output error!");
                        ErrorMsg errorMsg = new ErrorMsg(404, "语法分析错误，构造语法树时出错", StatusCode.ERROR_PARSER);
                        getErrorList().addError(errorMsg);
                    }

                    stack.pop();
                    for (int i = prod.getRight().size() - 1; i >= 0; i--) {
                        if (!prod.getRight().get(i).equals(Symbol.EMPTY_SYMBOL)) {
                            stack.push(prod.getRight().get(i));
                        }
                    }

                    String output = "Output:" + prod.getLeft().getName() + "->";
                    for (Symbol sym : prod.getRight()) {
                        output += sym.getName() + " ";
                    }
                    TableEntry entry = new TableEntry(matched, stack, buffer, output);
                    tableEntryList.add(entry);
                }
            }
            stackTop = stack.peek();
            bufferTop = buffer.peek();
        }
        if (!buffer.peek().equals(Symbol.TERMINAL_SYMBOL)) {
            System.out.println("Stack Empty Error.");
            ErrorMsg errorMsg = new ErrorMsg(405, "语法分析错误，栈已为空，输入还剩" + buffer.size() + "个符号未处理", StatusCode.ERROR_PARSER);
            getErrorList().addError(errorMsg);
            return null;
        }
        predictTable.setTableEntryList(tableEntryList);
        return syntaxTree;
    }
//
//    public static TD predict(String w, CFG cfg) {
//        //预测分析表
//        predictTable = new PredictTable();
//        List<TableEntry> tableEntryList = new ArrayList<>();
//        //语法树
//        TD.TNode<String> root = new TD.TNode<>();
//        root.setContent(cfg.getStartSymbol().getName());
//        TD syntaxTree = new TD(root);
//
//        LLParsingTable llParsingTable = new LLParsingTable(cfg);
//        Set<LLTableItem> itemSet = llParsingTable.getItemSet();
//        //已匹配
//        List<Symbol> matched = new ArrayList<>();
//        //栈
//        Stack<Symbol> stack = new Stack<>();
//        stack.push(Symbol.TERMINAL_SYMBOL);
//        stack.push(cfg.getStartSymbol());
//        //输入缓冲区
//        Stack<Symbol> buffer = new Stack<>();
//        buffer.push(Symbol.TERMINAL_SYMBOL);
//        String[] arr = w.split(" ");
//        Set<Symbol> allSymbols = cfg.getTerminalSet();
//        allSymbols.addAll(cfg.getNonTerminalSet());
//        for (int a = arr.length - 1; a >= 0; a--) {
//            boolean found = false;
//            String s = arr[a];
//            for (Symbol sym : allSymbols) {
//                if (sym.getName().equals(s)) {
//                    buffer.push(sym);
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                System.out.println("Error: Symbol not found.");
//                return null;
//            }
//        }
////        Stack<Symbol> tempStack = stack.clone();
//        TableEntry tableEntry = new TableEntry(null, stack, buffer, "");
//        tableEntryList.add(tableEntry);
//
//        Symbol stackTop = stack.peek();
//        Symbol bufferTop = buffer.peek();
//        while(!stackTop.equals(Symbol.TERMINAL_SYMBOL)) {
//            if (stackTop.equals(bufferTop)) {
//                System.out.println("Match:" + stackTop.getName());
//
//                TD.TNode<String> r = syntaxTree.getRoot();
//                List<TD.TNode> children = null;
//                Stack<TD.TNode<String>> matchStack = new Stack<>();
//                matchStack.push(r);
//                while (true) {
//                    if (r.getChildren().isEmpty() && !r.isMatched()) {
//                        break;
//                    } else {
//                        matchStack.pop();
//                        List<TD.TNode<String>> myChildren = r.getChildren();
//                        if (!(myChildren == null || myChildren.isEmpty())) {
//                            for (int i = myChildren.size() - 1; i >= 0; i--) {
//                                matchStack.push(myChildren.get(i));
//                            }
//                        }
//                        r = matchStack.peek();
//                    }
//                }
//
//                if (r.getContent().equals(stackTop.getName())) {
//                    r.setMatched(true);
//                } else {
//                    System.out.println("Tree match error!");
//                }
//
//                matched.add(stackTop);
//                stack.pop();
//                buffer.pop();
//
//                TableEntry entry = new TableEntry(matched, stack, buffer, "Match:" + stackTop.getName());
//                tableEntryList.add(entry);
//            } else if (stackTop.isTerminal()) {
//                System.out.println("Error: Terminal.");
//                return null;
//            } else {
//                Production prod = null;
//                for (LLTableItem item : itemSet) {
//                    if (item.getNonTerm().equals(stackTop) && item.getTerm().equals(bufferTop)) {
//                        prod = item.getValue();
//                        break;
//                    }
//                }
//                if (prod == null) {
//                    System.out.println("Error: LLTable item not found.");
//                    return null;
//                } else {
//                    System.out.print("Output:" + prod.getLeft().getName() + "->");
//                    for (Symbol sym : prod.getRight()) {
//                        System.out.print(sym.getName());
//                    }
//                    System.out.println();
//
//                    TD.TNode<String> r = syntaxTree.getRoot();
//                    List<TD.TNode> children = null;
//                    Stack<TD.TNode<String>> outputStack = new Stack<>();
//                    outputStack.push(r);
//                    while (true) {
//                        if (r.getChildren().isEmpty() && !r.isMatched()) {
//                            break;
//                        } else {
//                            outputStack.pop();
//                            List<TD.TNode<String>> myChildren = r.getChildren();
//                            if (!(myChildren == null || myChildren.isEmpty())) {
//                                for (int i = myChildren.size() - 1; i >= 0; i--) {
//                                    outputStack.push(myChildren.get(i));
//                                }
//                            }
//                            r = outputStack.peek();
//                        }
//                    }
//
//                    if (r.getContent().equals(prod.getLeft().getName())) {
//                        for (Symbol s : prod.getRight()) {
//                            TD.TNode<String> child = new TD.TNode<>();
//                            child.setContent(s.getName());
//                            if (s.equals(Symbol.EMPTY_SYMBOL)) {
//                                child.setMatched(true);
//                            }
//                            r.addChild(child);
//                        }
//                    } else {
//                        System.out.println("Tree output error!");
//                    }
//
//                    stack.pop();
//                    for (int i = prod.getRight().size() - 1; i >= 0; i--) {
//                        if (!prod.getRight().get(i).equals(Symbol.EMPTY_SYMBOL)) {
//                            stack.push(prod.getRight().get(i));
//                        }
//                    }
//
//                    String output = "Output:" + prod.getLeft().getName() + "->";
//                    for (Symbol sym : prod.getRight()) {
//                        output += sym.getName() + " ";
//                    }
//                    TableEntry entry = new TableEntry(matched, stack, buffer, output);
//                    tableEntryList.add(entry);
//                }
//            }
//            stackTop = stack.peek();
//            bufferTop = buffer.peek();
//        }
//        predictTable.setTableEntryList(tableEntryList);
//        return syntaxTree;
//    }

//    public static PredictTable getPredictTable() {
//        return predictTable;
//    }
}
