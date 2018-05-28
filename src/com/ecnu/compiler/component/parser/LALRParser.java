package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

/**
 * Bottom-Up Parsing, general LALR
 */
public class LALRParser extends Parser {

    public LALRParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }

    @Override
    public TD<Symbol> getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable) {
        return null;
    }

}
