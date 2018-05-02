package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.base.BaseLexer;
import com.ecnu.compiler.constant.StatusCode;

import java.io.File;

/**
 * 基于直接构造法的词法分析器
 *
 * @author Michael Chen
 * @date 2018-05-01 23:21
 */
public class DirectLexer extends BaseLexer {
    /**
     * 创建词法分析器
     *
     * @param file
     */
    public DirectLexer(File file) {
        super(file);
    }

    @Override
    public StatusCode next() {
        return null;
    }
}
