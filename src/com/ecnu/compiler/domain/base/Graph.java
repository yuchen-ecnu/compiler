package com.ecnu.compiler.domain.base;

import com.ecnu.compiler.domain.graph.Edge;
import com.ecnu.compiler.domain.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 图 基类
 * @author Michael Chen
 * @date 2018/04/24
 */
public class Graph {
    private List<Node> nodeList;

    public Graph() {
        this.nodeList = new ArrayList<>();
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
