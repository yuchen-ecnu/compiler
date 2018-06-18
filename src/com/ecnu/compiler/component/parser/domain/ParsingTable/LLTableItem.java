package com.ecnu.compiler.component.parser.domain.ParsingTable;

import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;

public class LLTableItem {
    private Symbol nonTerm;
    private Symbol term;
    private Production value;
    private boolean isConflict;

    public LLTableItem(Symbol nonTerm, Symbol term, Production value) {
        this.nonTerm = nonTerm;
        this.term = term;
        this.value = value;
        this.isConflict = false;
    }

    public Symbol getNonTerm() {
        return nonTerm;
    }

    public void setNonTerm(Symbol nonTerm) {
        this.nonTerm = nonTerm;
    }

    public Symbol getTerm() {
        return term;
    }

    public void setTerm(Symbol term) {
        this.term = term;
    }

    public Production getValue() {
        return value;
    }

    public void setValue(Production value) {
        this.value = value;
    }

    public boolean isConflict() {
        return isConflict;
    }

    public void setConflict(boolean conflict) {
        isConflict = conflict;
    }
}
