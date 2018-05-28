package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.ErrorList;

import java.util.ArrayList;
import java.util.List;

public class LanguageBuilder {
    //保存词法分析器构造过程部件的内部类
    static public class LexerHolder {
        //RE产生的NFA
        private NFA NFAFromRE;
        //前面NFA产生的DFA
        private DFA DFAFromNFA;
        //RE直接产生的DFA
        private DFA DFAFromRE;
        //DFAFromRE的最小化版本
        private DFA minStateDFAFromRE;
        //DFAFromNFA的最小化版本
        private DFA minStateDFAFromNFA;

        public NFA getNFAFromRE() {
            return NFAFromRE;
        }

        public DFA getDFAFromNFA() {
            return DFAFromNFA;
        }

        public DFA getDFAFromRE() {
            return DFAFromRE;
        }

        public DFA getMinStateDFAFromRE() {
            return minStateDFAFromRE;
        }

        public DFA getMinStateDFAFromNFA() {
            return minStateDFAFromNFA;
        }
    }


    //错误信息列表
    private ErrorList mErrorList;

    public LanguageBuilder() {
        mErrorList = new ErrorList();
    }

    public ErrorList getErrorList() {
        return mErrorList;
    }

    /**
     * 从RE字符串列表构造词法分析器需要的所有信息
     */
    public ArrayList<LexerHolder> buildLexerComponentsFromReStr(List<String> reStrList){
        ArrayList<LexerHolder> holders = new ArrayList<>();
        //todo RE格式的错误处理
        List<RE> reList = RE.buildREListFromStr(reStrList);
        for (RE re : reList){
            LexerHolder lexerHolder = new LexerHolder();
            lexerHolder.DFAFromRE = re.getDFADirectly();
            lexerHolder.NFAFromRE = re.getNFA();
            lexerHolder.DFAFromNFA = lexerHolder.NFAFromRE.getDFA();
            lexerHolder.minStateDFAFromRE = DFA.DFA2MinDFA(lexerHolder.DFAFromRE);
            lexerHolder.minStateDFAFromNFA = DFA.DFA2MinDFA(lexerHolder.DFAFromNFA);
            holders.add(lexerHolder);
        }
        return holders;
    }
}

    /*
    private static LanguageBuilder sLanguageBuilder;

    public static LanguageBuilder getInstance(){
        if (sLanguageBuilder == null){
            synchronized (LanguageBuilder.class){
                if (sLanguageBuilder == null)
                    sLanguageBuilder = new LanguageBuilder();
            }
        }
        return sLanguageBuilder;
    }*/