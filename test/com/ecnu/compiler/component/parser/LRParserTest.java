package com.ecnu.compiler.component.parser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LRParserTest {
    @Test
    public void buildParsingTable() throws Exception {
        String s = "1  2";
        String[] strings = s.split(" ");
        for(String ss : strings){
            System.out.println(ss);
        }
    }
}