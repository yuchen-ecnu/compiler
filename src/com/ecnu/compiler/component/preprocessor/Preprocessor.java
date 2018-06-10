package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.utils.TokenFilter;
import com.ecnu.compiler.constant.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 预处理器
 * @author Michael Chen
 * @date 2018/05/01
 */
public class Preprocessor {
    private Pattern mAnnotationPattern;
    private Pattern mPlaceholderPattern;

    public Preprocessor(String annotationRE){
        mAnnotationPattern = Pattern.compile(annotationRE);
        mPlaceholderPattern = Pattern.compile("[ \t]+");
    }

    public ArrayList<Token> preprocess(String text){
        //先把注释替换为一个空格，然后把多个制表空格等替换为一个空格,然后分行
        //替换注释
        String intermediateResult = mAnnotationPattern.matcher(text).replaceAll(Constants.NEWLINE);
        //替换占位符
        intermediateResult = mPlaceholderPattern.matcher(intermediateResult).replaceAll(Constants.SPACE_TOKEN);
        //分行
        String[] lineStrs = intermediateResult.split(Constants.NEWLINE);
        //预处理结果
        ArrayList<Token> tokenList = new ArrayList<>();
        for (int rowNum = 0; rowNum < lineStrs.length; rowNum++) {
            String[] items = lineStrs[rowNum].split(Constants.SPACE_TOKEN);
            for (String item : items) {
                if (item.length() > 0) {
                    Token token = new Token(null, item);
                    token.setRowNumber(rowNum + 1);
                    tokenList.add(token);
                }
            }
        }

        return tokenList;
    }
}
