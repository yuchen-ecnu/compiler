package com.ecnu.compiler.component.parser.base;

import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

/**
 * 语法分析器
 * @author Michael Chen
 * @date 2018/04/23
 */
public abstract class Parser {

    abstract public TD<Symbol> getSyntaxTree(SymbolTable symbolTable);
}
