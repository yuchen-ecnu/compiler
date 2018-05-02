package com.ecnu.compiler.component.lexer.domain.re2dfaUtils;

import java.util.Set;

public class DfaTraversal {
    
    private final DfaState q0;
    private DfaState curr;
    private char c;
    private final Set<String> input;
    
    public DfaTraversal(DfaState q0, Set<String> input){
        this.q0 = q0;
        this.curr = this.q0;
        this.input = input;
    }
    
    public boolean setCharacter(char c){
        if (!input.contains(c+"")){
            return false;
        }
        this.c = c;
        return true;
    }
    
    public boolean traverse(){
        curr = curr.getNextStateBySymbol(""+c);
        return curr.getAccepted();
    }
}
