package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;

import java.util.ArrayList;
import java.util.List;

public class LanguageBuilder {

    static public class DFAHolder{
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

    /**
     * 从RE字符串列表构造词法分析器需要的所有信息
     */
    static public ArrayList<DFAHolder> buildDFAListFromStr(List<String> reStrList){
        ArrayList<DFAHolder> holders = new ArrayList<>();
        //todo RE格式的错误处理
        List<RE> reList = RE.buildREListFromStr(reStrList);
        for (RE re : reList){
            DFAHolder dfaHolder = new DFAHolder();
            dfaHolder.DFAFromRE = re.getDFADirectly();
            dfaHolder.NFAFromRE = re.getNFA();
            dfaHolder.DFAFromNFA = dfaHolder.NFAFromRE.getDFA();
            dfaHolder.minStateDFAFromRE = DFA.DFA2MinDFA(dfaHolder.DFAFromRE);
            dfaHolder.minStateDFAFromNFA = DFA.DFA2MinDFA(dfaHolder.DFAFromNFA);
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