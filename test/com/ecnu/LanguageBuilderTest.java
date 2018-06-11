package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.Symbol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageBuilderTest {
    @Test
    public void LexerExample() {
        //创建一种随便的语言
        List<RE> reList = new ArrayList<>();
        reList.add(new RE("id", "a*|(b)|(x)", RE.NOMAL_SYMBOL));
        reList.add(new RE("if", "(!=)|(==)|(<)|(<=)|(>)|(>=)", RE.NOMAL_SYMBOL));
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

    @Test
    public void ParserTest(){
        //随便的一门语言
        List<String> productionStrList = new ArrayList<>();
        productionStrList.add("T->E");
        productionStrList.add("E->id");
        Symbol s2 = new Symbol("T");
        Symbol s1 = new Symbol("E");
        Symbol s3 = new Symbol("id");
        Symbol s4 = new Symbol("if");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        symbolSet.add(s4);
        CFG cfg = new CFG(productionStrList);

        LanguageBuilder languageBuilder = new LanguageBuilder();
        LanguageBuilder.ParserHolder parserHolder  =languageBuilder.buildPaserComponents(cfg);
        parserHolder.getLRParsingTable();
        parserHolder.getSLRParsingTable();
        parserHolder.getLALRParsingTable();
        parserHolder.getLLParsingTable();
    }

}