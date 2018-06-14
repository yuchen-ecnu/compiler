package com.ecnu.compiler.component.storage;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.domain.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 符号表
 *
 * @author Michael Chen
 * @date 2018-04-24 19:27
 */
public class SymbolTable {
    private List<Token> tokens;

    public SymbolTable() {
        this.tokens = new ArrayList<>();
    }

    public List<Token> getTokens() { return this.tokens; }

    public void addToken(Token token){
        tokens.add(token);
    }


    /**
     * 根据DFA列表和RE表达式列表构造符号表,方法暂时废用
     * @param dfaMap 用来匹配的DFA列表
     * @param lexemes 希望匹配的所有词素组成的文本，已经经过预处理，每个词素一个空格隔开.
     * @return 是否存在一个DFA能够匹配
     */
    /*public boolean build(Map<String, DFA> dfaMap, List<String> lexemes){
        //todo 尝试使用每个DFA匹配每个词素，匹配成功则向符号表中添加一个Token,现在的Token的属性只有一个，就是每个单词
        int count =0;
        for (String lexeme:lexemes){
            //遍历DFA
            for (Map.Entry<String,DFA> entry:dfaMap.entrySet()) {
                String key = entry.getKey();
                DFA dfa = entry.getValue();
                //匹配成功,添加词素
                if(dfa.match(lexeme)!=null){
                    int flag = 0;
                    for (Token token: this.tokens) {
                        //已包含该类型的token
                        if(token.getName().equals(key)){
                            //token.getAttrs().add(lexeme);
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0) {
                        //还没有该类型的token
                        List<Object> newAttr = new ArrayList<>();
                        newAttr.add(lexeme);
                        Token newToken = new Token(key, newAttr);
                        this.tokens.add(newToken);
                    }
                    break;
                }
            }
            count++;
        }
        return count == lexemes.size();
    }
    */
}
