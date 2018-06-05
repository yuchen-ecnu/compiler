package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.Compiler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompilerBuilderTest {

    @Test
    public void checkLanguage() {

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
        reList.add(new RE("if", "if"));
        reList.add(new RE("id", "a|(a|b)*"));
        List<String> productionStrList = new ArrayList<>();
        productionStrList.add("T -> id E id");
        productionStrList.add("E -> id | if T");
        //配置Config
        Config config = new Config();
        config.setExecuteType(Constants.EXECUTE_STAGE_BY_STAGE);
        //测试
        CompilerBuilder compilerBuilder = new CompilerBuilder();
        if (!compilerBuilder.checkLanguage(languageId)){
            compilerBuilder.prepareLanguage(languageId, Constants.LANGUAGE_JAVA, reList, productionStrList);
        }
        Compiler compiler = compilerBuilder.getCompilerInstance(languageId, config);
        //使用compiler
        //随便的一段代码
        String text = "/*11*/\nif//tt\n baab aabb if //test\n abab\t\t\taaab abbab";
        //初始化编译器
        compiler.prepare(text);
        //利用状态码判断是否达到了对应的步骤
        while (compiler.getStatus() != StatusCode.STAGE_PARSER){
            compiler.next();
            System.out.println("now status is: " + compiler.getStatus().getText());
        }

        if (compiler.getSymbolTable() != null)
            compiler.getSymbolTable().getTokens().forEach((token) -> System.out.println(token.getType()));
        else
            System.out.println("匹配失败");

        /* 当然你也可以这样来进行循环
        while (compiler.next() != StatusCode.STAGE_PARSER){
            System.out.println("now status is: " + compiler.getStatus().getText());
        }*/
        //结束了
        System.out.println("Ok!!");
    }
}