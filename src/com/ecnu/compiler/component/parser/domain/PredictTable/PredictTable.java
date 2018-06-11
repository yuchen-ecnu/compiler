package com.ecnu.compiler.component.parser.domain.PredictTable;

import com.ecnu.compiler.component.parser.domain.Symbol;

import java.util.List;
import java.util.Stack;

public class PredictTable {
    private List<TableEntry> tableEntryList;

    public List<TableEntry> getTableEntryList() {
        return tableEntryList;
    }

    public void setTableEntryList(List<TableEntry> tableEntryList) {
        this.tableEntryList = tableEntryList;
    }

    @Override
    public String toString() {
        String string = "";
        if (tableEntryList != null) {
            for (TableEntry e : tableEntryList) {
                string += e.toString() + "\n";
            }
        }
        return string;
    }
}
