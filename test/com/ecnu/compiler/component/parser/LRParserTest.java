package com.ecnu.compiler.component.parser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LRParserTest {
    @Test
    public void buildParsingTable() throws Exception {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        for (Integer i : list){
            if (list.size() < 5)
                list.add(list.size()+1);
        }

        for (Integer i : list){
            System.out.println(i);
        }
    }
}