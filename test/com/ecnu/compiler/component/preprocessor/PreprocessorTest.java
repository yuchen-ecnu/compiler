package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PreprocessorTest {

    @Test
    public void preprocess() {
        String text = "for (int i = 0; i < 10; i++) {\n}";
        System.out.println(text);

        Preprocessor preprocessor = new Preprocessor(Constants.JAVA_COMMENT);
        List<Token> results = preprocessor.preprocess(text);
        for (Token result : results){
            System.out.println(result.getStr() + ' ' + result.getRowNumber() + ' ' + result.getColPosition());
        }
    }
}