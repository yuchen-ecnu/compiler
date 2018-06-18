package com.ecnu.compiler.component.semantic.domain;

import com.ecnu.compiler.component.parser.domain.Symbol;


public class ActionSymbol extends Symbol {
    //符号类型
    private String mName;
    //动作
    private String mAction;

    public ActionSymbol(String name, String action) {
        super(name, true);
        mAction = action;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
