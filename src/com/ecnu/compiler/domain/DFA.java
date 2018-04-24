package com.ecnu.compiler.domain;

import com.ecnu.compiler.domain.base.Graph;
import com.ecnu.compiler.domain.graph.State;

/**
 * 确定性有限状态自动机
 * @author Michael Chen
 */
public class DFA extends Graph {

    private State curState;

    public DFA() { }

    public State take(char input){
        return null;
    }

    public boolean isEndState(){
        return curState != null && curState.isAccepted;
    }

}
