package com.ecnu.compiler.component.preprocessor;

import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.utils.TokenFilter;
import com.ecnu.compiler.constant.Constants;

/**
 * 预处理器
 * @author Michael Chen
 * @date 2018/05/01
 */
public class Preprocessor {
    private TokenFilter tokenFilter;

    public Preprocessor() {
        this.tokenFilter = new TokenFilter(new RE(Constants.IRRELEVANT_TOKEN));
    }
}
