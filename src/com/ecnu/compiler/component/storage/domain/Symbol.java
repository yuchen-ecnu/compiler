package com.ecnu.compiler.component.storage.domain;

/**
 * @author Michael Chen
 * @date 2018-05-01 15:03
 */
public class Symbol {
    public int id;
    public String character;

    public Symbol(String character, int id) {
        this.id = id;
        this.character = character;
    }
}
