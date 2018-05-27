package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.storage.SymbolTable;

import java.util.List;

public class Lexer {

    //用来识别的DFA列表
    List<DFA> mDFAList;

    public Lexer(List<DFA> DFAList) {
        mDFAList = DFAList;
    }

    /**
     * 根据DFA列表和RE表达式列表构造符号表
     * @param text 希望匹配的所有词素组成的文本，已经经过预处理，每个词素一个空格隔开.
     * @return 是否存在一个DFA能够匹配
     */
    public SymbolTable buildSymbolTable(String text){
        SymbolTable symbolTable = new SymbolTable();
        //todo 尝试使用每个DFA匹配每个词素，匹配成功则向符号表中添加一个Token,现在的Token的属性只有一个，就是每个单词
        return symbolTable;
    }
}
