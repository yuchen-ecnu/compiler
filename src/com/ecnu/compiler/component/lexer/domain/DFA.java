package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.util.ArrayList;
import java.util.List;

/**
 * 确定性有限状态自动机
 * @author Michael Chen
 */
public class DFA extends Graph {

    private List<DfaState> stateList;

    private List<State> states;

    private List<State> endStates;

    private State startDfaState;

    private State curState;

    private DfaState startState;

    private List<DfaState> endStateList;

    public DFA() { }

    public DFA(List<DfaState> dfaStateList) {
        this.setStateList(dfaStateList);
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getStartDfaState() {
        return startDfaState;
    }

    public void setStartDfaState(State startDfaState) {
        this.startDfaState = startDfaState;
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

    public int getMaxId(){
        int maxId=0;
        for(State s : this.states){
            if(s.getId()>maxId){
                maxId=s.getId();
            }
        }
        return maxId;
    }

    public State getStateById(int id) {
        for (State state : states) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public void setEndStateList(){
        this.endStates = new ArrayList<>();
        for(State state:states){
            if(state.getEdgeList().isEmpty()){
                state.isAccepted = true;
                endStates.add(state);
            }
        }
    }

    public List<State> getEndStates() {
        return endStates;
    }

    public void setEndStates(List<State> endStates) {
        this.endStates = endStates;
    }

    /**
     * minimize DFA
     * @return min DFA
     * @author Chen Jianing
     **/
    public DFA getMinDFA(){

        return null;
    }
}
