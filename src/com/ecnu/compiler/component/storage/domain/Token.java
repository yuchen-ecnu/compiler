package com.ecnu.compiler.component.storage.domain;

import java.util.List;

/**
 * @author Michael Chen
 * @date 2018-05-01 15:03
 */
public class Token {
    public String type;
    public List attrs;

    public Token(String type, List attrs) {
        this.type = type;
        this.attrs = attrs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Object> attrs) {
        this.attrs = attrs;
    }
}
