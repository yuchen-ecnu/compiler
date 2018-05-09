package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.RegexToDfa;

import java.util.*;

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
        if (startIndex > endIndex) {
            // 这里应该返回Null还是空NFA
            return rightNFA;
        }
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
                    default:
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
            if (exprList[i] == ')') {
                mark++;
            }
            if (exprList[i] == '('){
                mark --;
            }
            if (mark == 0) {
                return i;
            }
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
        DFA dfa = RegexToDfa.getDFA();
        dfa.print();
        return dfa;
    }

    /**
     * NFA 转 DFA
     * @param nfa NFA
     * @return DFA
     * @author Lucto
     */
    public static DFA NFA2DFA(NFA nfa){
        //用nfa的状态集合表示dfa中的状态
        Map<Set<State>,Integer> dfaStateSet = new HashMap<>();
        List<State> nfaStates = nfa.getStateList();
        Map<Integer,Set<State>> dfaStates = new HashMap<>();
        //求nfa中所有状态的ε闭包
        for(State nfaState : nfaStates){
            //根据nfa中每个状态的id保存该状态的ε闭包
            dfaStates.put(nfaState.getId(),getClosure(nfaState));
        }
        //用nfa中的状态来表示dfa的startState
        Set<State> dfaStartState = dfaStates.get(nfa.getStartState().getId());
        //初始化dfa和开始状态
        DFA dfa = new DFA();
        State startState = new State(0,false);
        dfaStateSet.put(dfaStartState,startState.getId());
        dfa.setStartDfaState(startState);
        //将开始状态存入dfa状态列表中
        List<State> dfaState = new ArrayList<>();
        dfaState.add(startState);
        dfa.setStates(dfaState);
        //获取dfa的下一状态
        Map<String,Set<State>> nextStates = getNewDfaStates(dfaStartState);
        Set<String> weights = nextStates.keySet();
        for(String weight : weights){
            //如果当前状态第一次出现
            if(!dfaStateSet.keySet().contains(nextStates.get(weight))){
                //扩充dfa
                drawDfa(startState,nextStates.get(weight),dfa.getMaxId()+1,dfa,dfaStateSet,weight);
            }else{
                drawExistDfa(startState,dfa.getStateById(dfaStateSet.get(nextStates.get(weight))),weight);
            }
        }
        dfa.getStates().get(dfa.getMaxId()).isAccepted=true;
        return dfa;
    }

    /**
     * 求nfa状态的ε闭包
     * @author Lucto
     **/
    private static Set<State> getClosure(State state){
        Set<State> closure = new HashSet<>();
        closure.clear();
        List<Edge> edges = state.getEdgeList();
        closure.add(state);
        for (Edge edge: edges) {
            if(edge.getWeight()=='\0' && !closure.contains(edge.getEndState())){
                closure.add(edge.getEndState());
                getNextClosure(edge.getEndState(),closure);
            }
        }
        return closure;
    }

    /**
     * 求nfa状态的ε闭包
     * @author Lucto
     **/
    private static void getNextClosure(State state,Set<State> closure){
        List<Edge> edges = state.getEdgeList();
        for (Edge edge: edges) {
            if(edge.getWeight()=='\0' && !closure.contains(edge.getEndState())){
                closure.add(edge.getEndState());
                getNextClosure(edge.getEndState(),closure);
            }
        }
    }

    /**
     * 获取dfa的下一状态
     * @author Lucto
     **/
    private static Map<String,Set<State>> getNewDfaStates(Set<State> curState){
        //获取所有可能的迁移条件
        Map<String,Set<State>> move = new HashMap<>();
        move.clear();
        for (State s: curState) {
            List<Edge> edges = s.getEdgeList();
            for(Edge edge: edges){
                if(edge.getWeight()!='\0' && move.containsKey(edge.getWeight()+"")){
                    move.get(edge.getWeight()+"").add(edge.getEndState());
                }else if(edge.getWeight()!='\0' && !move.containsKey(edge.getWeight()+"")){
                    Set<State> stateSet = new HashSet<>();
                    stateSet.clear();
                    stateSet.add(edge.getEndState());
                    move.put(edge.getWeight()+"",stateSet);
                }
            }
        }
        //根据迁移条件获取dfa的所有可能的下一状态
        Set<String> weights = move.keySet();
        Map<String,Set<State>> newDfaStates = new HashMap<>();
        newDfaStates.clear();
        for (String weight : weights){
            Set<State> newDfaState = new HashSet<>();
            newDfaState.clear();
            for(State state: move.get(weight)){
                newDfaState.addAll(getClosure(state));
            }
            newDfaStates.put(weight,newDfaState);
        }
        return newDfaStates;
    }

    /**
     * 扩充dfa
     * @author Lucto
     **/
    private static void drawDfa(State lastState,Set<State> curState,int id,DFA dfa,Map<Set<State>,Integer> dfaStateSet,String weight){
        //dfa中当前状态
        State state = new State(id,false);
        dfaStateSet.put(curState,state.getId());
        dfa.getStates().add(state);
        //dfa中上一状态到当前状态的边
        Edge edge = new Edge(lastState,state,weight.charAt(0));
        lastState.addEdge(edge);
        //获取dfa的下一状态
        Map<String,Set<State>> nextStates = new HashMap<>();
        nextStates.clear();
        nextStates = getNewDfaStates(curState);
        Set<String> weights = nextStates.keySet();
        for(String w : weights){
            //如果当前状态第一次出现
            if(!dfaStateSet.keySet().contains(nextStates.get(w))){
                //扩充dfa
                drawDfa(state,nextStates.get(w),dfa.getMaxId()+1,dfa,dfaStateSet,w);
            }else{
                drawExistDfa(state,dfa.getStateById(dfaStateSet.get(nextStates.get(w))),w);
            }
        }
    }

    /**
     * 扩展dfa中已存在的状态之间的边
     * @author Lucto
     **/
    private static void drawExistDfa(State curState,State nextState,String weight){
        Edge edge = new Edge(curState,nextState,weight.charAt(0));
        curState.addEdge(edge);
    }

    /**
     * minimize DFA
     * @param dfa DFA
     * @return DFA
     * @author Chen Jianing
     **/
    public static DFA DFA2MinDFA(DFA dfa){

        return null;
    }
}
