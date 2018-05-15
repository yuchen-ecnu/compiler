package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

/**
 * Top-Down Parsing, LL1
 *
 * @author Michael Chen
 * @date 2018-05-01 19:57
 */
public class LLParser extends Parser {

    @Override
    public TD<Symbol> getSyntaxTree(SymbolTable symbolTable) {
        return null;
    }
}
