package com.ecnu.compiler.component.lexer.domain.base;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.util.ArrayList;
import java.util.List;

/**
 * 图(连通) 基类
 * @author Michael Chen
 * @date 2018/04/24
 */
public class Graph {

    protected List<State> stateList;

    public Graph() {
        this.stateList = new ArrayList<>();
    }

    /**
     * 获取节点对象
     *
     * @param id 节点ID
     */
    public State get(int id) {
        for (State state : stateList) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public List<State> getStateList() {
        return stateList;
    }

    /**
     * 添加节点，此节点必须和原先的图连通
     *
     * @param state
     */
    public boolean addNode(State state, State parent, char input) {
        if (stateList.contains(state) || !stateList.contains(parent)) {
            return false;
        }
        //modified by Huge @data-5-2
        stateList.add(state);
        parent.addEdge(new Edge(parent, state, input));
        return true;
    }

    /**
     * 添加节点
     */
    public boolean addNode(State state) {
        if (stateList.contains(state))
            return false;
        stateList.add(state);
        state.id = stateList.size() - 1;
        return true;
    }

    /**
     * 添加边
     */
    public boolean addEdge(State start, State end, char input) {
        if (start != null && end != null && stateList.contains(start) && stateList.contains(end)) {
            start.addEdge(new Edge(start, end, input));
            return true;
        }
        return false;
    }

    /**
     * 重设ID
     */
    public void resetId() {
        for (int i = 0; i < stateList.size(); i++) {
            stateList.get(i).id = i;
        }

    }
}