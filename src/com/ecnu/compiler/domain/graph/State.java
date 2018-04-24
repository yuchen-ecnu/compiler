package com.ecnu.compiler.domain.graph;

import com.ecnu.compiler.constant.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 图的节点
 * @author Michael Chen
 * @date 2018/04/24
 */
public class State {
    /**状态ID*/
    public int id;
    /**标识特殊状态*/
    public boolean isAccepted;
    /**可迁移路径集合*/
    private List<Edge> edgeList;

    public State(int id,boolean isAccepted) {
        this.id = id;
        this.isAccepted = isAccepted;
        this.edgeList = new ArrayList<>();
    }

    public boolean addEdge(Edge edge){
        if(edgeList.contains(edge)){ return false; }
        else {
            edgeList.add(edge);
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        State state = (State) o;

        return this.id == state.id;
    }

    @Override
    public int hashCode() {
        return 31 * id;
    }
}
