package com.ecnu;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.LALRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.LRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.SLRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.storage.ErrorList;

import java.util.ArrayList;
import java.util.List;

public class LanguageBuilder {


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
    public ArrayList<LexerHolder> buildLexerComponentsFromReStr(List<RE> reList){
        ArrayList<LexerHolder> holders = new ArrayList<>();
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

    /**
     * 从CFG构造语法分析器需要的所有信息
     */
    public ParserHolder buildParserComponents(List<String> productionList){
        CFG cfg = new CFG(productionList, mErrorList);
        ParserHolder holder = new ParserHolder();
        holder.mLLParsingTable = new LLParsingTable(cfg);
        holder.mLRParsingTable = new LRParserBuilder().buildParsingTable(cfg);
        holder.mSLRParsingTable = new SLRParserBuilder().buildParsingTable(cfg);
        holder.mLALRParsingTable = new LALRParserBuilder().buildParsingTable(cfg);
        return holder;
    }


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

    static public class ParserHolder {
        //LL分析表
        private LLParsingTable mLLParsingTable;
        //LR分析表
        private LRParsingTable mLRParsingTable;
        //SLR分析表
        private LRParsingTable mSLRParsingTable;
        //LALR分析表
        private LRParsingTable mLALRParsingTable;

        public LLParsingTable getLLParsingTable() {
            return mLLParsingTable;
        }

        public LRParsingTable getLRParsingTable() {
            return mLRParsingTable;
        }

        public LRParsingTable getSLRParsingTable() {
            return mSLRParsingTable;
        }

        public LRParsingTable getLALRParsingTable() {
            return mLALRParsingTable;
        }
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