package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.constant.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PreprocessorTest {

    @Test
    public void preprocess() {
        String text = "  11  /* 22 33 4 */  aaa    //bb\ncc\n//t  \nddd /*a*/ t\n";
        System.out.println(text);

        Preprocessor preprocessor = new Preprocessor(Constants.JAVA_COMMENT);
        List<String> results = preprocessor.preprocess(text);
        for (String result : results){
            System.out.println(result);
        }
    }
}