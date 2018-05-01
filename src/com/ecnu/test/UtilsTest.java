package com.ecnu.test;

import com.ecnu.compiler.component.lexer.Utils;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void RE2NFA() throws Exception {
    }

    @Test
    public void RE2DFA() throws Exception {
        RE expression = new RE("(a|b)*abb");
        DFA dfa = Utils.RE2DFA(expression);
    }

    @Test
    public void NFA2DFA() throws Exception {
    }

    @Test
    public void DFA2MinDFA() throws Exception {
    }

}