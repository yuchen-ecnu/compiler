package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.RegexToDfa;

public class Utils {


    /**
     * RE 转 NFA
     * @param re RE表达式: Constants.xxx, 假设RE合法
     * @return NFA
     * @author Huge
     */
    public static NFA RE2NFA(RE re){
        //输入暂时使用String, 因为RE暂时没有实现
        String expression = re.getExpression();
        char[] exprList = expression.toCharArray();
        boolean[] transferMark = new boolean[exprList.length];
        for (int i = 0; i < exprList.length; i++) {
            transferMark[i] = false;
            if (exprList[i] == '\\'){
              transferMark[i+1] = true;
              i++;
            }
        }
        NFA result = buildNFA(exprList, transferMark, 0, exprList.length - 1);
        result.resetId();
        return result;
    }

    private static NFA buildNFA(char[] exprList, boolean[] transferMark, int startIndex, int endIndex){
        NFA rightNFA = new NFA();
        if (startIndex > endIndex)
            return rightNFA; // 这里应该返回Null还是空NFA
        for (int i = endIndex; i >= startIndex; ) {
            if (i > startIndex && !transferMark[i] &&
                    (exprList[i] == '*' || exprList[i] == '|'
                            || exprList[i] == '\\' || exprList[i] == ')')){
                switch (exprList[i]) {
                    case '*':
                        if (exprList[i - 1] == ')') {
                            //如果是右括号
                            //找到左括号
                            int leftBracketIndex = findLeftBracket(exprList, startIndex, i - 1);
                            //递归构造子NFA
                            NFA leftNFA = buildNFA(exprList, transferMark, leftBracketIndex + 1, i - 1);
                            //构造kleene闭包
                            buildKleene(leftNFA, rightNFA);
                            //将当前目标改到左括号左边
                            i = leftBracketIndex - 1;
                        } else {
                            //如果是其他字符
                            buildKleene(exprList[i - 1], rightNFA);
                            i -= 2;
                        }
                        break;
                    case '|':
                        NFA leftNFA = buildNFA(exprList, transferMark, startIndex, i - 1);
                        //把左边的所有部分构成的NFA和右边进行OR运算
                        buildOr(leftNFA, rightNFA);
                        return rightNFA;
                    case '\\':
                        //未被标记的\是起转义作用，直接忽略
                        i--;
                        break;
                    case ')':
                        //找到左括号
                        int leftBracketIndex = findLeftBracket(exprList, startIndex, i - 1);
                        //递归构造子NFA
                        leftNFA = buildNFA(exprList, transferMark, leftBracketIndex + 1, i - 1);
                        //连接左右NFA
                        buildLink(leftNFA, rightNFA);
                        //将当前目标改到左括号左边
                        i = leftBracketIndex - 1;
                        break;
                }
            } else {
                //普通连接字符
                buildLink(exprList[i], rightNFA);
                i--;
            }
        }
        return rightNFA;
    }

    //找到
    private static int findLeftBracket(char[] exprList, int leftBound, int rightBound){
        int mark = 1;
        for (int i = rightBound; i >= leftBound; i--) {
            if (exprList[i] == ')')
                mark ++;
            if (exprList[i] == '(')
                mark --;
            if (mark == 0)
                return i;
        }
        return -1;
    }

    //thompson构造法, 结果都合并到right NFA中，所以返回值可以不用。
    private static void buildOr (NFA left, NFA right){
        //添加两个节点
        State newStart = new State(0, false);
        State newEnd = new State(0, false);
        right.addNode(newStart);
        right.addNode(newEnd);
        //添加4条边，合并左右
        right.addEdge(newStart, right.getStartState(), '\0');
        right.addEdge(right.getEndState(), newEnd, '\0');
        right.mergeGraph(left, newStart, left.getStartState(), '\0');
        right.addEdge(left.getEndState(), newEnd, '\0');
        //设置起始终结状态
        right.setStartState(newStart);
        right.setEndState(newEnd);
    }

    private static void buildKleene(char left, NFA right){
        //构造左NFA
        NFA leftNFA = new NFA();
        buildLink(left, leftNFA);
        //进行kleene操作
        buildKleene(leftNFA, right);
    }

    private static void buildKleene(NFA left, NFA right){
        //对left做kleen操作
        //添加两个节点
        State newStart = new State(0, false);
        State newEnd = new State(0, false);
        left.addNode(newStart);
        left.addNode(newEnd);
        //添加4条边
        left.addEdge(newStart, left.getStartState(), '\0');
        left.addEdge(newStart, newEnd, '\0');
        left.addEdge(left.getEndState(), newEnd, '\0');
        left.addEdge(left.getEndState(), left.getStartState(), '\0');
        //设置起始终止节点
        left.setStartState(newStart);
        left.setEndState(newEnd);
        //将左右连接
        buildLink(left, right);
    }

    private static void buildLink(char left, NFA right){
        State newState = new State(0, false);
        right.addNode(newState);
        right.addEdge(newState, right.getStartState(), left);
        right.setStartState(newState);
    }
    private static void buildLink(NFA left, NFA right){
        right.mergeGraph(left, left.getEndState(), right.getStartState(), '\0');
        right.setStartState(left.getStartState());
    }



    /**
     * RE 转 DFA
     * @param expression RE表达式: Constants.xxx
     * @return DFA
     * @author Meng Xin
     */
    public static DFA RE2DFA(RE expression){
        RegexToDfa.initialize(expression);
        return RegexToDfa.getDFA();
    }

    /**
     * NFA 转 DFA
     * @param nfa NFA
     * @return DFA
     * @author Lucto
     */
    public static DFA NFA2DFA(NFA nfa){

        return null;
    }


    /**
     * minimize DFA
     * @param dfa DFA
     * @return DFA
     * @author Chen Jianing
     */
    public static DFA DFA2MinDFA(DFA dfa){

        return null;
    }
}
