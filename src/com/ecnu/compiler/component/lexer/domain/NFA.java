package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;

import java.util.*;

/**
 * 非确定性有限状态自动机
 * @author Michael Chen
 */
public class NFA extends Graph {

    private State startState;
    private State endState;

    public NFA() {
        State defaultState = new State(0, true);
        addNode(defaultState);
        startState = defaultState;
        endState = defaultState;
    }

    /**
     * 合并并两个图，需要注意这里重设了state的ID，出于安全考虑，被合并的NFAToMerge之后不应该再被使用
     * @author Huge
     * @param NFAToMerge 待合并图
     * @param start,end,input 连接两图的边
     */
    public boolean mergeGraph(NFA NFAToMerge, State start, State end, char input){
        //保证连接两图的边存在
        List<State> stateList = getStateList();
        if (NFAToMerge != null && start != null && end != null
                && ((stateList.contains(start) && NFAToMerge.getStateList().contains(end))
                                || (stateList.contains(end) && NFAToMerge.getStateList().contains(start)))){
            //合并所有节点
            stateList.addAll(NFAToMerge.getStateList());
            //添加连接边
            start.addEdge(new Edge(start, end, input));
            //重设ID
            //resetId();
            return true;
        }
        return false;
    }

    /**
     * 设置NFA的起始状态
     */
    public boolean setStartState(State state){
        if (getStateList().contains(state)){
            startState = state;
            return true;
        }
        return false;
    }

    public State getStartState() {
        return startState;
    }

    /**
     * 设置NFA的终止状态,NFA只有唯一的终止
     */
    public boolean setEndState(State state){
        if (getStateList().contains(state)){
            endState.isAccepted = false;
            endState = state;
            endState.isAccepted = true;
            return true;
        }
        return false;
    }

    public State getEndState() {
        return endState;
    }

    /**
     * NFA 转 DFA
     * @return DFA
     * @author Lucto
     */
    public DFA getDFA(){
        //用nfa的状态集合表示dfa中的状态
        Map<Set<State>,Integer> dfaStateSet = new HashMap<>();
        List<State> nfaStates = getStateList();
        Map<Integer,Set<State>> dfaStates = new HashMap<>();
        //求nfa中所有状态的ε闭包
        for(State nfaState : nfaStates){
            //根据nfa中每个状态的id保存该状态的ε闭包
            dfaStates.put(nfaState.getId(),getClosure(nfaState));
        }
        //用nfa中的状态来表示dfa的startState
        Set<State> dfaStartState = dfaStates.get(getStartState().getId());
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
}
