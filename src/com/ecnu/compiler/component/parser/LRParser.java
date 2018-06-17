package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.*;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.TableEntry;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;

import java.util.*;

/**
 * Bottom-Up Parsing, general LR
 *
 * @author Michael Chen
 * @date 2018-05-01 19:58
 */
public class LRParser extends Parser {

    public LRParser(CFG CFG, ParsingTable parsingTable, ErrorList errorList) {
        super(CFG, parsingTable, errorList);
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
        tableHead.add("Stack");
        tableHead.add("Input");
        tableHead.add("Action");
        tableHead.add("Output");
        predictTable.setTableHead(tableHead);
        //LR解析表
        LRParsingTable lrParsingTable = (LRParsingTable)parsingTable;
        //所有的产生式
        List<Production> productionList = cfg.getAllProductions();
        //状态栈
        Stack<Integer> stateStack = new Stack<>();
        stateStack.push(0);
        //语法子树栈
        Stack<TD.TNode> syntaxTreeStack = new Stack<>();
        //当前分析的输入Token列表
        List<Token> tokenList = symbolTable.getTokens();
        //添加终结符
        tokenList.add(new Token(Constants.TERMINAL_TOKEN));

        //获得SymbolTable所有token组成的String
        StringBuilder stringBuilder = new StringBuilder();
        for (Token token : tokenList){
            stringBuilder.append(token).append(" ");
        }
        String allInputStr = stringBuilder.toString().trim();


        //当前输入符号的index以及其字符串内容
        int curIndex = 0;
        String curInputStr = tokenList.get(curIndex).getType();
        while (curIndex < tokenList.size()) {
            //获取表项
            LRParsingTable.TableItem tableItem = lrParsingTable.getItem(stateStack.peek(), curInputStr);
            if (tableItem == null){
                predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                        allInputStr.substring(curIndex, allInputStr.length()), "Error: Cannot find item in action table",
                        ""));
                getErrorList().addErrorMsg("语法分析错误， Action表项("
                                + stateStack.peek() + ", " + curInputStr + ")找不到", StatusCode.ERROR_PARSER);
                return null;
            }
            switch (tableItem.getOperate()){
                case LRParsingTable.SHIFT:
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            allInputStr.substring(curIndex, allInputStr.length()), "Shift " + tableItem.getValue(),
                            ""));
                    //如果是Shift，则移入相应状态以及添加一个新的树节点
                    stateStack.push(tableItem.getValue());
                    syntaxTreeStack.push(new TD.TNode(curInputStr));
                    //更新当前处理的输入符号
                    curInputStr = tokenList.get(++curIndex).getType();
                    break;

                case LRParsingTable.REDUCE:
                    //获取相应产生式
                    Production production = productionList.get(tableItem.getValue());
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            allInputStr.substring(curIndex, allInputStr.length()), "Reduce " + production,
                            production.toString()));
                    //构造新的树节点
                    TD.TNode parentNode = new TD.TNode(production.getLeft().getName());
                    List<Symbol> right = production.getRight();
                    if (right.size() > 0){
                        parentNode.setProductionId(production.getId());
                        for (int i = 0; i < right.size(); i++) {
                            stateStack.pop();
                            TD.TNode childNode = syntaxTreeStack.pop();
                            parentNode.addChild(childNode);
                        }
                    }
                    //反向孩子节点
                    parentNode.reverseChildren();
                    //将新节点放入节点栈
                    syntaxTreeStack.push(parentNode);
                    //查GOTO表
                    LRParsingTable.TableItem gotoTableItem = lrParsingTable.getItem(stateStack.peek(), parentNode.getContent());
                    if (gotoTableItem == null || gotoTableItem.getOperate() != LRParsingTable.GOTO){
                        predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                                allInputStr.substring(curIndex, allInputStr.length()), "Error: Cannot find item in goto table", ""));
                        getErrorList().addErrorMsg("语法分析错误， Goto表项("
                                + stateStack.peek() + ", " + curInputStr + ")找不到", StatusCode.ERROR_PARSER);
                        return null;
                    }
                    //把新状态插入状态栈
                    stateStack.push(gotoTableItem.getValue());
                    break;

                case LRParsingTable.ACCEPT:
                    //返回构造完成的树
                    //记录预测表
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            allInputStr.substring(curIndex, allInputStr.length()), "Accept ", ""));
                    return new TD(syntaxTreeStack.pop());

                default:
                    predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                            allInputStr.substring(curIndex, allInputStr.length()), "System Error", ""));
                    getErrorList().addErrorMsg("语法分析内部错误（未知的表项内容）", StatusCode.ERROR_PARSER);
                    return null;
            }
        }
        //如果输入都完了还没到达，那么匹配就失败了，语法有错误。
        predictTable.addTableEntry(new TableEntry(stateStack, syntaxTreeStack,
                allInputStr.substring(curIndex, allInputStr.length()), "Error: Cannot find item in action table", ""));
        getErrorList().addErrorMsg("语法分析错误， 无法抵达接受项，分析失败", StatusCode.ERROR_PARSER);
        return null;
    }
}
