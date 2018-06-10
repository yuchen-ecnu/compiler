package com.ecnu.compiler.component.parser.domain.ParserBuilder;

import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;

public class LALRParserBuilder extends LRParserBuilder {
    @Override
    public LRParsingTable buildParsingTable(CFG cfg) {
        LRParsingTable lrParsingTable = super.buildParsingTable(cfg);
        //删除同质化的行
        return lrParsingTable;
    }
}
