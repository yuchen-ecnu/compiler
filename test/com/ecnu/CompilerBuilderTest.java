package com.ecnu;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.component.storage.domain.Token;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.Compiler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        reList.add(new RE("+", "\\+", RE.SPILT_SYMBOL));
        reList.add(new RE("*", "\\*", RE.SPILT_SYMBOL));
        reList.add(new RE("if", "if", RE.NOMAL_SYMBOL));
        reList.add(new RE("id", "a|(a|b)*", RE.NOMAL_SYMBOL));
        reList.add(new RE("a", "a", RE.NOMAL_SYMBOL));
        reList.add(new RE("b", "b", RE.NOMAL_SYMBOL));
        reList.add(new RE("c", "c", RE.NOMAL_SYMBOL));
        reList.add(new RE("d", "d", RE.NOMAL_SYMBOL));
        List<String> productionStrList = new ArrayList<>();
//        productionStrList.add("T -> ( id ) E id");
//        productionStrList.add("E -> id | if T");
//        productionStrList.add("E -> E + T");
//        productionStrList.add("E -> T");
//        productionStrList.add("T -> T * F");
//        productionStrList.add("T -> F");
//        productionStrList.add("F -> ( E )");
//        productionStrList.add("F -> id");
        productionStrList.add("S->A a | b A c | d c | b d a");
        productionStrList.add("A -> d");
        Map<String, String> agActionMap = new HashMap<>();
        agActionMap.put("P", "E.in = id0.digit");
        agActionMap.put("Q", "E.s = if.s");
        List<String> agProductionStrList = new ArrayList<>();
        agProductionStrList.add("T -> ( id )  P E id");
        agProductionStrList.add("E -> id | if T Q");
        //配置Config
        Config config = new Config();
        config.setExecuteType(Constants.EXECUTE_STAGE_BY_STAGE);
        config.setParserAlgorithm(Constants.PARSER_SLR);
        //测试
        CompilerBuilder compilerBuilder = new CompilerBuilder();
        if (!compilerBuilder.checkLanguage(languageId)){
            Language language = compilerBuilder.prepareLanguage(languageId, reList, productionStrList,
                    agProductionStrList, agActionMap);
            if (compilerBuilder.getErrorList().getErrorMsgList().size() > 0){
                System.out.println("Errror");
                compilerBuilder.getErrorList().printAllErrorAndClear();
            }
        }
        Compiler compiler = compilerBuilder.getCompilerInstance(languageId, config);
        //使用compiler
        //随便的一段代码
        //String text = "(if) if (if) aabb \n if if";
        String text = "aa * aa + aa";
        //初始化编译器
        compiler.prepare(text);
        //利用状态码判断是否达到了对应的步骤
        while (compiler.getStatus().getCode() > 0 && compiler.getStatus() != StatusCode.STAGE_SEMANTIC_ANALYZER){
            compiler.next();
            System.out.println("now status is: " + compiler.getStatus().getText());
        }
        if (compiler.getStatus().getCode() < 0){
            System.out.println("编译失败");
            compiler.getErrorList().printAllErrorAndClear();
            return;
        }
        Compiler.TimeHolder timeHolder = compiler.getTimeHolder();
        System.out.println("预处理时间：" + timeHolder.getPreprocessorTime());
        System.out.println("词法处理器时间：" + timeHolder.getLexerTime());
        System.out.println("语法处理器时间：" + timeHolder.getParserTime());
        System.out.println("语义处理器时间：" + timeHolder.getSemanticTime());

        if (compiler.getSymbolTable() != null)
            compiler.getSymbolTable().getTokens().forEach((token) -> {
                System.out.println(token.getType() + " " + token.getRowNumber() + " " + token.getColPosition());
            });
        else{
            System.out.println("词法匹配失败");
            compilerBuilder.getErrorList().printAllErrorAndClear();
            return;
        }

        if (compiler.getSyntaxTree() != null){
            TD syntaxTree = compiler.getSyntaxTree();
            TD.printTree(syntaxTree);
        }else {
            System.out.println("语法分析失败");
            compilerBuilder.getErrorList().printAllErrorAndClear();
            return;
        }

        if (compiler.getActionList() != null){
            compiler.getActionList().forEach(System.out::println);
        }else {
            System.out.println("语义分析失败");
            compilerBuilder.getErrorList().printAllErrorAndClear();
            return;
        }
        System.out.println("Ok!!");
    }
}