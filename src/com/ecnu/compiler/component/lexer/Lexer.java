package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lexer {

    //用来识别的DFA列表
    List<DFA> mDFAList;

    public Lexer(List<DFA> DFAList) {
        mDFAList = DFAList;
    }

    /**
     * 根据DFA列表和RE表达式列表构造符号表
     * @param lexemes 希望匹配的所有词素组成的文本，已经经过预处理，每个词素一个空格隔开.
     * @return 是否存在一个DFA能够匹配
     */
    public SymbolTable buildSymbolTable(List<String> lexemes){
        SymbolTable symbolTable = new SymbolTable();
        //todo 尝试使用每个DFA匹配每个词素，匹配成功则向符号表中添加一个Token,现在的Token的属性只有一个，就是每个单词
        for (String lexeme:lexemes){
            //遍历DFA
            for (DFA dfa : mDFAList){
                if (dfa.match(lexeme) != null){
                    //匹配成功
                    List<String> attrs = new ArrayList<>();
                    attrs.add(lexeme);
                    symbolTable.addToken(new Token(dfa.getName(), attrs));
                }
            }
        }
        return symbolTable;
    }
}
