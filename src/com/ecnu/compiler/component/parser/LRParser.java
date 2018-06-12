package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.*;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.TableEntry;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Constants;

import java.util.*;

/**
 * Bottom-Up Parsing, general LR
 *
 * @author Michael Chen
 * @date 2018-05-01 19:58
 */
public class LRParser extends Parser {

    public LRParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }


    /**
     * 根据解析表和符号表构造语法树
     * @param parsingTable
     * @param symbolTable
     * @return
     */
    @Override
    protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable, PredictTable predictTable) {
        //添加预测表表头
        ArrayList<String> tableHead = new ArrayList<>();
        tableHead.add("States");
        tableHead.add("Symbols");
        tableHead.add("Input");
        tableHead.add("Action");
        predictTable.setTableHead(tableHead);
        //LR解析表
        LRParsingTable lrParsingTable = (LRParsingTable)parsingTable;
        //所有的产生式
        List<Production> productionList = cfg.getAllProductions();
        //状态栈
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0);
        //语法子树栈
        Stack<TD.TNode<String>> syntaxTreeStack = new Stack<>();
        //当前分析的输入Token列表
        List<Token> tokenList = symbolTable.getTokens();
        //添加终结符
        tokenList.add(new Token(Constants.TERMINAL_TOKEN));

        //当前输入符号的index以及其字符串内容
        int curIndex = 0;
        String curInputStr = tokenList.get(curIndex).getType();
        while (curIndex < tokenList.size()) {
            //获取表项
            LRParsingTable.TableItem tableItem = lrParsingTable.getItem(stateStack.peek(), curInputStr);
            if (tableItem == null){
                //todo 处理查表失败
                System.out.println("查表失败");
                return null;
            }
            switch (tableItem.getOperate()){
                case LRParsingTable.SHIFT:
                    //如果是Shift，则移入相应状态以及添加一个新的树节点
                    stateStack.push(tableItem.getValue());
                    syntaxTreeStack.push(new TD.TNode<>(curInputStr));
                    //更新当前处理的输入符号
                    curInputStr = tokenList.get(++curIndex).getType();
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            symbolTable.getTokens(), curIndex, "Shift " + tableItem.getValue()));
                    break;

                case LRParsingTable.REDUCE:
                    //获取相应产生式
                    Production production = productionList.get(tableItem.getValue());
                    //构造新的树节点
                    TD.TNode<String> parentNode = new TD.TNode<>(production.getLeft().getType());
                    List<Symbol> right = production.getRight();
                    for (int i = 0; i < right.size(); i++) {
                        stateStack.pop();
                        TD.TNode<String> childNode = syntaxTreeStack.pop();
                        parentNode.addChild(childNode);
                    }
                    //反向孩子节点
                    parentNode.reverseChildren();
                    //将新节点放入节点栈
                    syntaxTreeStack.push(parentNode);
                    //查GOTO表
                    LRParsingTable.TableItem gotoTableItem = lrParsingTable.getItem(stateStack.peek(), parentNode.getContent());
                    if (gotoTableItem == null || gotoTableItem.getOperate() != LRParsingTable.GOTO){
                        //todo 处理查表失败
                        System.out.println("查表失败");
                        return null;
                    }
                    //把新状态插入状态栈
                    stateStack.push(gotoTableItem.getValue());
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            symbolTable.getTokens(), curIndex, "Reduce " + production));
                    break;

                case LRParsingTable.ACCEPT:
                    //返回构造完成的树
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            symbolTable.getTokens(), curIndex, "Accept "));
                    return new TD(syntaxTreeStack.pop());

                default:
                    //todo 处理查表失败
                    System.out.println("查表失败");
                    return null;
            }
        }
        //如果输入都完了还没到达，那么匹配就失败了，语法有错误。
        //todo 处理语法错误
        System.out.println("语法错误");
        return null;
    }
}
