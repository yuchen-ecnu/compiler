package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    //用来识别的DFA列表
    List<DFA> mDFAList;
    //用来识别的RE列表
    List<RE> mREList;

    public Lexer(List<DFA> DFAList) {
        mDFAList = DFAList;
    }

    public Lexer(List<RE> reList, int i){
        mREList = reList;
    }



    /**
     * 构造符号表
     * @param lexemes 希望匹配的所有词素组成的文本列表
     * @return 符号表
     */
    public SymbolTable buildSymbolTable(List<String> lexemes){
        //return buildSymbolTableByDFA(lexemes);
        return buildSymbolTableByJavaRE(lexemes);
    }

    /**
     * 根据DFA列表构造符号表
     * @param lexemes 希望匹配的所有词素组成的文本列表
     * @return 符号表
     */
    private SymbolTable buildSymbolTableByDFA(List<String> lexemes){
        if (mDFAList == null)
            return null;
        SymbolTable symbolTable = new SymbolTable();
        tag: for (String lexeme:lexemes){
            //遍历DFA
            for (DFA dfa : mDFAList){
                if (dfa.match(lexeme) != null){
                    //匹配成功
                    List<String> attrs = new ArrayList<>();
                    attrs.add(lexeme);
                    symbolTable.addToken(new Token(dfa.getName(), attrs));
                    continue tag;
                }
            }
            //todo 匹配失败错误处理
            return null;
        }
        return symbolTable;
    }

    /**
     * 使用JAVA自带的RE匹配来实现匹配
     * @param lexemes 希望匹配的所有词素组成的文本列表
     * @return
     */
    private SymbolTable buildSymbolTableByJavaRE(List<String> lexemes){
        if (mREList == null)
            return null;
        SymbolTable symbolTable = new SymbolTable();
        ArrayList<Pattern> patternList = new ArrayList<>();
        for (RE re : mREList){
            patternList.add(Pattern.compile(re.getExpression()));
        }

        tag: for (String lexeme:lexemes) {
            Iterator<RE> reIterator = mREList.iterator();
            Iterator<Pattern> patternIterator = patternList.iterator();
            while (reIterator.hasNext()) {
                String name = reIterator.next().getName();
                Pattern pattern =  patternIterator.next();
                Matcher matcher = pattern.matcher(lexeme);

                if (matcher.matches()){
                    //匹配成功
                    List<String> attrs = new ArrayList<>();
                    attrs.add(lexeme);
                    symbolTable.addToken(new Token(name, attrs));
                    continue tag;
                }
            }
            //todo 匹配失败错误处理
            return null;
        }
        return symbolTable;
    }
}
