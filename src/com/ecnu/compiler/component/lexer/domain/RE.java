package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.RegexToDfa;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装正则表达式，实体类
 * 保存一个RE对应的名字和其表达式
 */
public class RE {

    //从String构造RE的列表,现在直接在组件外部构造
    /*
    static public ArrayList<RE> buildREListFromStr(List<String> reStrList){
        ArrayList<RE> reList = new ArrayList<>();
        for (String reStr : reStrList){
            String[] reNameExp = reStr.split(" ");
            if (reNameExp.length != 2){
                System.out.println("RE格式错误");
                return null;
            }
            RE re = new RE(reNameExp[0], reNameExp[1]);
            reList.add(re);
        }
        return reList;
    }*/

    //RE对应符号名
    private String name;
    //RE对应表达式
    private String expression;

    public RE(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof RE)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        RE re = (RE)object;
        //name相等则说明必然是同一个RE
        return name.equals(re.name);
    }

    /**
     * 获取 RE 对应的 NFA
     * @return NFA
     * @author Huge
     */
    public NFA getNFA(){
        if(expression==null) { return null; }

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
        result.setName(name);
        return result;
    }

    /**
     * RE 转 DFA
     * @return DFA
     * @author Meng Xin
     */
    public DFA getDFADirectly(){
        String realExp = expression + "#";
        RegexToDfa.initialize(realExp);
        DFA dfa = RegexToDfa.getDFA();
        dfa.setName(name);
        //dfa.print();
        return dfa;
    }

    /**
     * RE 转 DFA
     * @return DFA
     * @author Meng Xin
     */
    public DFA getDFAIndirect(){
        NFA nfa = getNFA();
        return nfa.getDFA();
    }


    /**
     * RE 转 NFA 辅助函数
     */
    private NFA buildNFA(char[] exprList, boolean[] transferMark, int startIndex, int endIndex){
        NFA rightNFA = new NFA();
        if (startIndex > endIndex) { return rightNFA; }
        for (int i = endIndex; i >= startIndex; ) {
            boolean flag = i > startIndex && !transferMark[i]
                    && (exprList[i] == '*' || exprList[i] == '|' || exprList[i] == '\\' || exprList[i] == ')');
            if (!flag) {
                //普通连接字符
                buildLink(exprList[i], rightNFA);
                i--;
                continue;
            }
            NFA leftNFA;
            int leftBracketIndex;
            switch (exprList[i]) {
                case '*':
                    if (exprList[i - 1] != ')') {
                        buildKleene(exprList[i - 1], rightNFA);
                        i -= 2;
                        break;
                    }
                    //如果是右括号
                    //找到左括号
                    leftBracketIndex = findLeftBracket(exprList, startIndex, i - 1);
                    //递归构造子NFA
                    leftNFA = buildNFA(exprList, transferMark, leftBracketIndex + 1, i - 1);
                    //构造kleene闭包
                    buildKleene(leftNFA, rightNFA);
                    //将当前目标改到左括号左边
                    i = leftBracketIndex - 1;
                    break;
                case '|':
                    leftNFA = buildNFA(exprList, transferMark, startIndex, i - 1);
                    //把左边的所有部分构成的NFA和右边进行OR运算
                    buildOr(leftNFA, rightNFA);
                    return rightNFA;
                case '\\':
                    //未被标记的\是起转义作用，直接忽略
                    i--;
                    break;
                case ')':
                    //找到左括号
                    leftBracketIndex = findLeftBracket(exprList, startIndex, i - 1);
                    //递归构造子NFA
                    leftNFA = buildNFA(exprList, transferMark, leftBracketIndex + 1, i - 1);
                    //连接左右NFA
                    buildLink(leftNFA, rightNFA);
                    //将当前目标改到左括号左边
                    i = leftBracketIndex - 1;
                    break;
                default:
                    break;
            }
        }
        return rightNFA;
    }

    //找到
    private int findLeftBracket(char[] exprList, int leftBound, int rightBound){
        int mark = 1;
        for (int i = rightBound; i >= leftBound; i--) {
            if (exprList[i] == ')') { mark++; }
            if (exprList[i] == '('){ mark --; }
            if (mark == 0) { return i; }
        }
        return -1;
    }

    //thompson构造法, 结果都合并到right NFA中，所以返回值可以不用。
    private void buildOr (NFA left, NFA right){
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

    private void buildKleene(char left, NFA right){
        //构造左NFA
        NFA leftNFA = new NFA();
        buildLink(left, leftNFA);
        //进行kleene操作
        buildKleene(leftNFA, right);
    }

    private void buildKleene(NFA left, NFA right){
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

    private void buildLink(char left, NFA right){
        State newState = new State(0, false);
        right.addNode(newState);
        right.addEdge(newState, right.getStartState(), left);
        right.setStartState(newState);
    }
    private void buildLink(NFA left, NFA right){
        right.mergeGraph(left, left.getEndState(), right.getStartState(), '\0');
        right.setStartState(left.getStartState());
    }

}
