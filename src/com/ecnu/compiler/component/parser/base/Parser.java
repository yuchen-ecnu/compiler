package com.ecnu.compiler.component.parser.base;

import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.SymbolTable;

import java.util.List;

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
    private List<String> mErrorList;

    public Parser(CFG CFG, ParsingTable parsingTable) {
        mCfg = CFG;
        mParsingTable = parsingTable;
    }

    public TD buildSyntaxTree(SymbolTable symbolTable, PredictTable predictTable){
        return getSyntaxTree(mCfg, mParsingTable, symbolTable, predictTable);
    }

    abstract protected TD getSyntaxTree(CFG cfg, ParsingTable parsingTable, SymbolTable symbolTable, PredictTable predictTable);

    public List<String> getErrorList() {
        return mErrorList;
    }

    public void setErrorList(List<String> mErrorList) {
        this.mErrorList = mErrorList;
    }
}
