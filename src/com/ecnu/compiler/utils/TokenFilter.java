package com.ecnu.compiler.utils;

import com.ecnu.compiler.component.lexer.domain.RE;

import java.util.regex.Pattern;

import static com.ecnu.compiler.constant.Constants.EMPTY_STRING;

/**
 * 字符过滤器
 *
 * @author Michael Chen
 * @date 2018-05-01 15:15
 */
public class TokenFilter {
    private Pattern pattern;

    /**
     * 创建过滤器
     * @param strategy 该过滤器使用的策略
     */
    public TokenFilter(RE strategy) {
        this.pattern = Pattern.compile(strategy.getExpression());
    }


    /**
     * 直接使用String创建过滤器
     * @param strategy
     */
    public TokenFilter(String strategy) {
        this.pattern = Pattern.compile(strategy);
    }

    /**
     * 执行过滤程序    trim()去除首尾空格（行）
     * @param input 输入字符流
     * @return 返回过滤后的字符流
     */
    public String execute (String input){
        return pattern.matcher(input).replaceAll(EMPTY_STRING).trim();
    }
}
