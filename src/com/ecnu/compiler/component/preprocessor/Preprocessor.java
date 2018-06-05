package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.component.lexer.domain.RE;
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

    public Preprocessor(String annotationRE){
        mAnnotationPattern = Pattern.compile(annotationRE);
    }

    public ArrayList<String> preprocess(String text){
        //先把注释替换为一个空格，然后把多个制表换行空格等替换为一个空格。
        //替换注释
        String intermediateResult = mAnnotationPattern.matcher(text).replaceAll(Constants.SPACE_TOKEN);
        //替换占位符
        intermediateResult = Pattern.compile("[ \t\n]+")
                .matcher(intermediateResult).replaceAll(Constants.SPACE_TOKEN);

        //利用空格划分字符串
        return new ArrayList<>(Arrays.asList(intermediateResult.trim().split(Constants.SPACE_TOKEN)));
    }
}
