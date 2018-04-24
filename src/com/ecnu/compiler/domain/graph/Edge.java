package com.ecnu.compiler.domain.graph;

/**
 * 有向图的边
 * @author Michael Chen
 * @date 2018/04/24
 */
public class Edge {
    private Node startNode;
    private Node endNode;
    private char weight;

    public Edge() { }

    public Edge(Node startNode, Node endNode, char weight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;

        return startNode.equals(edge.startNode) && endNode.equals(edge.endNode);
    }

    @Override
    public int hashCode() {
        int result = startNode.hashCode();
        result = 31 * result + endNode.hashCode();
        return result;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public char getWeight() {
        return weight;
    }

    public void setWeight(char weight) {
        this.weight = weight;
    }
}
