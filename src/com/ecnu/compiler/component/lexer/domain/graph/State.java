package com.ecnu.compiler.component.lexer.domain.graph;

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

    public boolean removeEdge(Edge edge) {
        if (!edgeList.contains(edge)) {
            return false;
        } else {
            edgeList.remove(edge);
            return true;
        }
    }

    public int getId() {
        return id;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    @Override
    public boolean equals(Object o) {
        /*if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        State state = (State) o;

        return this.id == state.id;*/
        return this == o;
    }

    @Override
    public int hashCode() {
        return 31 * id;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", isAccepted=" + isAccepted +
                '}';
    }
}

