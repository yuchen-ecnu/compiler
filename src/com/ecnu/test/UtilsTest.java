package com.ecnu.test;

import com.ecnu.compiler.component.lexer.Utils;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void RE2NFA() throws Exception {
        RE expression = new RE("(a|b)*d");
        NFA nfa = Utils.RE2NFA(expression);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nfa.getStartState().getId()).append(" <-> ")
                .append(nfa.getEndState().getId()).append("  in ").append(nfa.getStateList().size()).append('\n');
        int count = 0;
        for (State state : nfa.getStateList()){
            for (Edge edge : state.getEdgeList()){
                stringBuilder.append(edge.getStartState().getId()).append(" -").append(edge.getWeight()).append("> ")
                        .append(edge.getEndState().getId()).append('\n');
                count++;
            }
        }
        stringBuilder.append("total edges: ").append(count);
        System.out.println(stringBuilder.toString());
    }

    @Test
    public void RE2DFA() throws Exception {
        RE expression = new RE("(a|b)*abb#");
        DFA dfa = Utils.RE2DFA(expression);
    }

    @Test
    public void NFA2DFA() throws Exception {
    }

    @Test
    public void DFA2MinDFA() throws Exception {
    }

}