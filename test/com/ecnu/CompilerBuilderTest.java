package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.Compiler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CompilerBuilderTest {

    @Test
    public void checkLanguage() {
        String test = "((";
        Pattern p = Pattern.compile("\\(");
        Matcher matcher = p.matcher(test);
        while (matcher.find()){
            System.out.println(" " + matcher.start() + " " + matcher.end());
        }
    }

    @Test
    public void prepareLanguage() {

    }

    @Test
    public void getCompilerInstance() {

    }

    @Test
    public void runExample() {
        //创建一种随便的语言
        int languageId = 0;
        List<RE> reList = new ArrayList<>();
        reList.add(new RE("(", "\\(", RE.SPILT_SYMBOL));
        reList.add(new RE(")", "\\)", RE.SPILT_SYMBOL));
        reList.add(new RE("if", "if", RE.NOMAL_SYMBOL));
        reList.add(new RE("id", "a|(a|b)*", RE.NOMAL_SYMBOL));
        List<String> productionStrList = new ArrayList<>();
        productionStrList.add("T->id E id");
        productionStrList.add("E->id|if T");
        //配置Config
        Config config = new Config();
        config.setExecuteType(Constants.EXECUTE_STAGE_BY_STAGE);
        config.setParserAlgorithm(Constants.PARSER_LR);
        //测试
        CompilerBuilder compilerBuilder = new CompilerBuilder();
        if (!compilerBuilder.checkLanguage(languageId)){
            compilerBuilder.prepareLanguage(languageId, reList, productionStrList);
        }
        Compiler compiler = compilerBuilder.getCompilerInstance(languageId, config);
        //使用compiler
        //随便的一段代码
        String text = "aa if ba //sdfsdfs\naabb \n bb ab";
        //初始化编译器
        compiler.prepare(text);
        //利用状态码判断是否达到了对应的步骤
        while (compiler.getStatus() != StatusCode.STAGE_SEMANTIC_ANALYZER){
            compiler.next();
            System.out.println("now status is: " + compiler.getStatus().getText());
        }
        Compiler.TimeHolder timeHolder = compiler.getTimeHolder();
        System.out.println("预处理时间：" + timeHolder.getPreprocessorTime());
        System.out.println("词法编译器时间：" + timeHolder.getLexerTime());
        System.out.println("语法编译器时间：" + timeHolder.getParserTime());

        if (compiler.getSymbolTable() != null)
            compiler.getSymbolTable().getTokens().forEach((token) -> {
                System.out.println(token.getType() + " " + token.getRowNumber() + " " + token.getColPosition());
            });
        else
            System.out.println("词法匹配失败");

        if (compiler.getSyntaxTree() != null){
            TD syntaxTree = compiler.getSyntaxTree();
            TD.printTree(syntaxTree);
        }else {
            System.out.println("语法分析失败");
        }


        /* 当然你也可以这样来进行循环
        while (compiler.next() != StatusCode.STAGE_PARSER){
            System.out.println("now status is: " + compiler.getStatus().getText());
        }*/
        //结束了
        System.out.println("Ok!!");
    }
}