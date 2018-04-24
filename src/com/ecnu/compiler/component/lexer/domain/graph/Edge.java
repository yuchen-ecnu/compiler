package com.ecnu.compiler.component.lexer.domain.graph;

/**
 * 有向图的边
 * @author Michael Chen
 * @date 2018/04/24
 */
public class Edge {
    /**起始状态*/
    private State startState;
    /**目标状态*/
    private State endState;
    /**接受字符*/
    private char weight;

    public Edge() { }

    public Edge(State startState, State endState, char weight) {
        this.startState = startState;
        this.endState = endState;
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

        return startState.equals(edge.startState) && endState.equals(edge.endState);
    }

    @Override
    public int hashCode() {
        int result = startState.hashCode();
        result = 31 * result + endState.hashCode();
        return result;
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public State getEndState() {
        return endState;
    }

    public void setEndState(State endState) {
        this.endState = endState;
    }

    public char getWeight() {
        return weight;
    }

    public void setWeight(char weight) {
        this.weight = weight;
    }
}
