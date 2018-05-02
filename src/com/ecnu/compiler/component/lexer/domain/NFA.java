package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;

import java.util.List;

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
}
