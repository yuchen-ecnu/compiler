package com.ecnu.compiler.component.lexer;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.component.storage.domain.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    //用来识别的DFA列表
    List<DFA> mDFAList;
    //用来识别的Pattern列表
    ArrayList<RE> mSplitREList;
    ArrayList<RE> mNormalREList;
    //用来识别的Pattern列表
    ArrayList<Pattern> mSplitPatList;
    ArrayList<Pattern> mNormalPatList;

    public Lexer(List<DFA> DFAList) {
        mDFAList = DFAList;
    }

    public Lexer(List<RE> reList, int i){
        mSplitPatList = new ArrayList<>();
        mNormalPatList = new ArrayList<>();
        mSplitREList = new ArrayList<>();
        mNormalREList = new ArrayList<>();
        for (RE re : reList){
            if (re.getType() == RE.SPILT_SYMBOL){
                mSplitREList.add(re);
                mSplitPatList.add(Pattern.compile(re.getExpression()));
            } else if (re.getType() == RE.NOMAL_SYMBOL){
                mNormalREList.add(re);
                mNormalPatList.add(Pattern.compile(re.getExpression()));
            }
        }
    }



    /**
     * 构造符号表
     * @param lexemes 希望匹配的所有词素组成的文本列表
     * @return 符号表
     */
    public SymbolTable buildSymbolTable(List<Token> lexemes){
        //return buildSymbolTableByDFA(lexemes);
        return buildSymbolTableByJavaRE(lexemes);
    }

    /**
     * 根据DFA列表构造符号表
     * @param lexemes 希望匹配的所有词素组成的文本列表
     * @return 符号表
     */
    private SymbolTable buildSymbolTableByDFA(List<String> lexemes){
        if (mDFAList == null)
            return null;
        SymbolTable symbolTable = new SymbolTable();
        tag: for (String lexeme:lexemes){
            //遍历DFA
            for (DFA dfa : mDFAList){
                if (dfa.match(lexeme) != null){
                    //匹配成功
                    symbolTable.addToken(new Token(dfa.getName()));
                    continue tag;
                }
            }
            //todo 匹配失败错误处理
            return null;
        }
        return symbolTable;
    }

    /**
     * 使用JAVA自带的RE匹配来实现匹配
     * @param tokenList 希望匹配的所有词素组成的文本列表
     * @return
     */
    private SymbolTable buildSymbolTableByJavaRE(List<Token> tokenList){
        if (mSplitPatList == null || mNormalPatList == null)
            return null;

        //处理分割符号
        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);
            if (token.getType() != null)
                continue;
            //使用分割符号进行分割
            Iterator<Pattern> patternIterator = mSplitPatList.iterator();
            Iterator<RE> reIterator = mSplitREList.iterator();
            while (patternIterator.hasNext()) {
                Pattern pattern = patternIterator.next();
                RE re = reIterator.next();
                String toMatch = token.getStr();
                Matcher matcher = pattern.matcher(toMatch);
                //如果找到了一个分隔符
                if (matcher.find()) {
                    //移除当前token
                    tokenList.remove(i);
                    //当前记录位置
                    int itemIndex = 0;
                    //当前添加位置
                    int addPosition = i;
                    do {
                        if (matcher.start() > itemIndex){
                            Token newToken = new Token(null, toMatch.substring(itemIndex, matcher.start()));
                            tokenList.add(addPosition, newToken);
                            addPosition ++;
                        }
                        Token splitToken = new Token(re.getName(), matcher.group());
                        tokenList.add(addPosition, splitToken);
                        addPosition ++;
                        itemIndex = matcher.end();
                    } while (matcher.find());
                    if (itemIndex < toMatch.length()){
                        tokenList.add(addPosition, new Token(null, toMatch.substring(itemIndex, toMatch.length())));
                    }
                    i--;
                    break;
                }
            }
        }

        //构造符号表
        SymbolTable symbolTable = new SymbolTable();
        tag:
        for (Token token : tokenList){
            if (token.getType() != null){
                symbolTable.addToken(token);
                continue ;
            }
            Iterator<Pattern> patternIterator = mNormalPatList.iterator();
            Iterator<RE> reIterator = mNormalREList.iterator();
            while (patternIterator.hasNext()) {
                String name = reIterator.next().getName();
                Pattern pattern =  patternIterator.next();
                Matcher matcher = pattern.matcher(token.getStr());
                if (matcher.matches()){
                    //匹配成功
                    symbolTable.addToken(new Token(name, token.getStr()));
                    continue tag;
                }
            }
            //todo 匹配失败错误处理
            return null;
        }
        return symbolTable;
    }
}
