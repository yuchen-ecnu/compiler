package com.ecnu.compiler.component.lexer.domain.base;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.util.ArrayList;
import java.util.List;

/**
 * 图 基类
 * @author Michael Chen
 * @date 2018/04/24
 */
public class Graph {

    private List<State> stateList;

    public Graph() {
        this.stateList = new ArrayList<>();
    }

    /**
     * 获取节点对象
     * @param id 节点ID
     */
    public State get(int id){
        for (State state : stateList) {
            if(state.id==id){
                return state;
            }
        }
        return null;
    }

    /**
     * 添加节点
     * @param state
     */
    public boolean addNode(State state, State parent, char input){
        if(stateList.contains(state)||!stateList.contains(parent)){ return false; }
        parent.addEdge(new Edge(parent,state,input));
        return true;
    }

    public List<State> getStateList() {
        return stateList;
    }
}
