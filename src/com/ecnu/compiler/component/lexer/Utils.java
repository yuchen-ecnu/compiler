package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.RegexToDfa;

public class Utils {


    /**
     * RE 转 NFA
     * @param expression RE表达式: Constants.xxx
     * @return NFA
     * @author Hu JiaBin
     */
    public static NFA RE2NFA(RE expression){

        return null;
    }

    /**
     * RE 转 DFA
     * @param expression RE表达式: Constants.xxx
     * @return DFA
     * @author Meng Xin
     */
    public static DFA RE2DFA(RE expression){
        RegexToDfa.initialize(expression);
        return RegexToDfa.getDFA();
    }

    /**
     * NFA 转 DFA
     * @param nfa NFA
     * @return DFA
     * @author Lucto
     */
    public static DFA NFA2DFA(NFA nfa){

        return null;
    }


    /**
     * minimize DFA
     * @param dfa DFA
     * @return DFA
     * @author Chen Jianing
     */
    public static DFA DFA2MinDFA(DFA dfa){

        return null;
    }
}
