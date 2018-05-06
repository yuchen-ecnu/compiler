package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.util.List;

/**
 * 确定性有限状态自动机
 * @author Michael Chen
 */
public class DFA extends Graph {

    private List<DfaState> stateList;

    private State curState;

    private DfaState startState;

    private List<DfaState> endStateList;

    public DFA() { }

    public DFA(List<DfaState> dfaStateList) {
        this.setStateList(dfaStateList);
    }

    public List<DfaState> getDfaStateList() {
        return stateList;
    }

    public void setStateList(List<DfaState> dfaStateList) {
        this.stateList = dfaStateList;
    }

    public void setStartState(DfaState startState) {
        this.startState = startState;
    }

    public void setEndStateList(List<DfaState> endStateList) {
        this.endStateList = endStateList;
    }

    public State take(char input){
        return null;
    }

    public boolean isEndState(){
        return curState != null && curState.isAccepted;
    }

    public void print() {
        String states = "States:\n";
        String edges = "Edges:\n";
        String endStates = "End States:\n";
        for(DfaState s : stateList) {
            states += s.toString();
            for(Edge e : s.getEdgeList()){
                edges += e.toStringForDfa();
            }
        }
        for(DfaState s : endStateList) {
            endStates = endStates + s.getName() + "\n";
        }
        System.out.println(states);
        System.out.println(edges);
        System.out.println("Start State:\n" + startState.getName());
        System.out.println(endStates);
    }
}
