package com.ecnu;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.CacheManager.LanguageCache;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.LALRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.LRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParserBuilder.SLRParserBuilder;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LRParsingTable;
import com.ecnu.compiler.component.semantic.domain.AG;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.Compiler;

import java.util.List;
import java.util.Map;

/**
 * 编译器  --定义对外的所有接口
 * （最终作为Jar包导入WEB程序中）
 *
 * @author Huge
 * @date  2018/05/27
 */
public class CompilerBuilder {
    //缓存
    private LanguageCache mLanguageCache;
    //错误列表
    private ErrorList mErrorList;

    public CompilerBuilder() {
        //获得缓存实例
        mLanguageCache = LanguageCache.getInstance();
        mErrorList = new ErrorList();
    }

    public ErrorList getErrorList() {
        return mErrorList;
    }

    /**
     * 检查语言是否已经缓存
     * @param languageId
     * @return
     */
    public boolean checkLanguage(int languageId){
        return mLanguageCache.check(languageId);
    }

    /**
     * 构建语言
     * @param languageId 语言的ID
     * @return 构造的语言
     */
    public Language prepareLanguage(int languageId, List<RE> reList, List<String> productionStrList,
            List<String> AGProductionStrList, Map<String, String> actionMap){
        //创建语言信息
        Language language = new Language(languageId);
        if (reList != null){
            //保存RE列表
            language.setREList(reList);
            //构造DFA
            /*List<DFA> dfaList = DFA.buildDFAListFromREList(reList);
            if (dfaList == null) {
                mErrorList.addErrorMsg("构造DFA失败，RE有错误", StatusCode.ERROR_INIT);
                return null;
            }
            language.setDFAList(dfaList);*/
        }
        if (productionStrList != null && productionStrList.size() > 0){
            //构造CFG
            CFG cfg = new CFG(productionStrList, mErrorList);
            if (!cfg.isSucessfulBuild()) {
                mErrorList.addErrorMsg("构造CFG失败，产生式有错误", StatusCode.ERROR_INIT);
                return null;
            }
            language.setCFG(cfg);
            //构造各解析表
            language.setLLParsingTable(new LLParsingTable(cfg));
            LRParsingTable lrParsingTable;
            boolean canParser = false;
            if ((lrParsingTable = new LRParserBuilder().buildParsingTable(cfg)) != null){
                language.setLRParsingTable(lrParsingTable);
                canParser = true;
            } else {
                getErrorList().addErrorMsg("构造LR解析表失败", StatusCode.ERROR_INIT);
            }
            if ((lrParsingTable = new SLRParserBuilder().buildParsingTable(cfg)) != null){
                language.setLRParsingTable(lrParsingTable);
                canParser = true;
            } else {
                getErrorList().addErrorMsg("构造SLR解析表失败", StatusCode.ERROR_INIT);
            }
            if ((lrParsingTable = new LALRParserBuilder().buildParsingTable(cfg)) != null){
                language.setLRParsingTable(lrParsingTable);
                canParser = true;
            } else {
                getErrorList().addErrorMsg("构造LALR解析表失败", StatusCode.ERROR_INIT);
            }
            if (!canParser){
                getErrorList().addErrorMsg("该语言不可进行语法分析", StatusCode.ERROR_INIT);
                return null;
            }
        }
        if (actionMap != null && actionMap.size() > 0){
            //构造ag
            AG ag = new AG(AGProductionStrList, actionMap, mErrorList);
            if (!ag.isSuccessfulBuild()) {
                mErrorList.addErrorMsg("构造CFG失败，产生式有错误", StatusCode.ERROR_INIT);
                return null;
            }
            language.setAG(ag);
        }
        //保存language到缓存
        mLanguageCache.saveToCache(language);
        return language;
    }

    /**
     * 根据语言ID获得编译器实例，如果语言还未缓存，则返回NULL，并添加错误信息
     * @param languageId
     * @param config
     * @return
     */
    public Compiler getCompilerInstance(int languageId, Config config){
        if (!checkLanguage(languageId)){
            mErrorList.addError(new ErrorMsg(0, "", StatusCode.ERROR_INIT));
            return null;
        }
        Language language = mLanguageCache.getFromCache(languageId);
        Compiler compiler = new Compiler(language, config, mErrorList);
        return compiler.getStatus() == StatusCode.ERROR_INIT ? null : compiler;
    }
}
