package com.ecnu.compiler.component.lexer.base;

import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.constant.StatusCode;

import java.io.File;

/**
 * 词法分析器(去除注释、无用的回车换行找到包含的文件等)
 *
 * @author Michael Chen
 * @date 2018/04/23
 */
public abstract class BaseLexer {

    /**符号表*/
    protected SymbolTable symbolTable;
    /**输入文件流*/
    protected File file;

    /**符号识别处理器集合*/
    private DFA identifierChecker;
    private DFA constantChecker;
    private DFA reservedChecker;
    private DFA operatorChecker;

    /**
     * 创建词法分析器
     */
    public BaseLexer(File file) {
        this.file = file;
        this.symbolTable = new SymbolTable();
    }

    /** 单步执行 */
    public abstract StatusCode next();
}
