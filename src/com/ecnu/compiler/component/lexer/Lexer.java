package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.SymbolTable;

/**
 * 词法分析器(去除注释、无用的回车换行找到包含的文件等)
 * @author Michael Chen
 * @date 2018/04/23
 */
public class Lexer {

    //符号表
    private SymbolTable symbolTable;

    /**
     * 创建词法分析器
     */
    public Lexer() {
        symbolTable = new SymbolTable();

    }
}
