package com.ecnu.compiler.component.parser.base;

import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;

/**
 * 语法分析器
 * @author Michael Chen
 * @date 2018/04/23
 */
public abstract class Parser {

    //CFG
    private CFG mCfg;
    //语法分析表
    private ParsingTable mParsingTable;
    //错误列表
    private ErrorList mErrorList;

    public Parser(CFG CFG, ParsingTable parsingTable, ErrorList errorList) {
        mCfg = CFG;
        mParsingTable = parsingTable;
        mErrorList = errorList;
    }

    protected ErrorList getErrorList() {
        return mErrorList;
    }

    public TD buildSyntaxTree(SymbolTable symbolTable, PredictTable predictTable){
        return getSyntaxTree(mCfg, mParsingTable, symbolTable, predictTable);
    }

    abstract protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable, PredictTable predictTable);
}
