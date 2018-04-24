package com.ecnu.compiler.domain.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 图的节点
 * @author Michael Chen
 */
public class Node {
    private int id;
    private List<Edge> edgeList;

    public Node(int id) {
        this.id = id;
        this.edgeList = new ArrayList<>();
    }

    public int getId() {
        return id;
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

        Node node = (Node) o;

        return this.id == node.id;
    }

    @Override
    public int hashCode() {
        return 31 * id;
    }
}
