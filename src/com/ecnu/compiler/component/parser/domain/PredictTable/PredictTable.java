package com.ecnu.compiler.component.parser.domain.PredictTable;

import com.ecnu.compiler.component.parser.domain.Symbol;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PredictTable {
    private List<TableEntry> tableEntryList;

    public PredictTable() {
        tableEntryList = new ArrayList<>();
    }

    public void addTableEntry(TableEntry tableEntry){
        tableEntryList.add(tableEntry);
    }

    public List<TableEntry> getTableEntryList() {
        return tableEntryList;
    }

    public void setTableEntryList(List<TableEntry> tableEntryList) {
        this.tableEntryList = tableEntryList;
    }

    @Override
    public String toString() {
        String string = "";
        for (TableEntry e : tableEntryList) {
            string += e.toString() + "\n";
        }
        return string;
    }
}
