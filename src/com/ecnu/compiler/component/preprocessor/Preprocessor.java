package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.utils.TokenFilter;
import com.ecnu.compiler.constant.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 预处理器
 * @author Michael Chen
 * @date 2018/05/01
 */
public class Preprocessor {
    private TokenFilter tokenFilter;

    public Preprocessor() {
        this.tokenFilter = new TokenFilter(Constants.IRRELEVANT_TOKEN);
    }

    public List<String> preprocess(String text){
        //todo 没做任何预处理,简单的用空格符分词
        return new ArrayList<String>(Arrays.asList(text.split(" ")));
    }
}
