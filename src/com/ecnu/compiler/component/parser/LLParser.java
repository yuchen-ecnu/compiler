package com.ecnu.compiler.component.parser;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
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

    public LLParser(CFG CFG, ParsingTable parsingTable) {
        super(CFG, parsingTable);
    }

    @Override
    protected TD<Symbol> getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable) {
        return null;
    }
}
