package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
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

    public DFA() { }

    public DFA(List<DfaState> dfaStateList) {
        this.setStateList(dfaStateList);
    }

    public void setStateList(List<DfaState> dfaStateList) {
        this.stateList = dfaStateList;
    }

    public State take(char input){
        return null;
    }

    public boolean isEndState(){
        return curState != null && curState.isAccepted;
    }

}
