package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.State;

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
