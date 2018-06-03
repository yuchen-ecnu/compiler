package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LanguageBuilderTest {
    @Test
    public void runExample() {
        //创建一种随便的语言
        int languageId = 0;
        List<RE> reList = new ArrayList<>();
        reList.add(new RE("id", "aab"));
        reList.add(new RE("if", "if"));
        //测试
        LanguageBuilder languageBuilder = new LanguageBuilder();
        List<LanguageBuilder.LexerHolder> lexerHolders = languageBuilder.buildLexerComponentsFromReStr(reList);
        //从Holders里面拿数据
        for (LanguageBuilder.LexerHolder holder : lexerHolders){
            DFA directDfa = holder.getDFAFromRE();
            NFA nfa = holder.getNFAFromRE();
            DFA indirectDfa = holder.getDFAFromNFA();
            //实际上下面两个应该是一样的
            DFA minDirectDFA = holder.getMinStateDFAFromRE();
            DFA minIndirectDFA = holder.getMinStateDFAFromNFA();
            //当然，拿出来之后你想干嘛就随便你了。。。。
        }
    }

}