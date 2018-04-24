package com.ecnu.compiler.domain.base;

import com.ecnu.compiler.domain.graph.Edge;
import com.ecnu.compiler.domain.graph.Node;

import java.util.List;

public class Graph {
    private List<Node> nodeList;

    public Graph() {
        this.nodeList = nodeList;
    }

    /**
     * 获取节点对象
     * @param id 节点ID
     */
    public Node get(int id){
        for (Node node : nodeList) {
            if(node.getId()==id){
                return node;
            }
        }
        return null;
    }

    /**
     * 添加节点
     * @param node
     */
    public boolean addNode(Node node,Node parent,char weight){
        if(nodeList.contains(node)||!nodeList.contains(parent)){ return false; }
        parent.addEdge(new Edge(parent,node,weight));
        return true;
    }
}
