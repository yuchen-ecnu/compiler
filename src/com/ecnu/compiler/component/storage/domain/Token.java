package com.ecnu.compiler.component.storage.domain;

import java.util.List;

/**
 * @author Michael Chen
 * @date 2018-05-01 15:03
 */
public class Token {
    public String type;
    public List<Object> attrs;

    public Token(String type, List<Object> attrs) {
        this.type = type;
        this.attrs = attrs;
    }
}
