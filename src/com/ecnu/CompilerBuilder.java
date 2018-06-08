package com.ecnu;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.CacheManager.LanguageCache;
import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.Compiler;

import java.util.List;

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
     * @param languageId
     * @return
     */
    public void prepareLanguage(int languageId, List<RE> reList, List<String> productionStrList){
        //todo 构造时出现问题之后单独做一个EXCEPTION来处理，现在先假设RE没毛病。
        //创建语言信息
        Language language = new Language(languageId);
        //保存RE列表
        language.setREList(reList);
        //构造DFA
        List<DFA> dfaList = DFA.buildDFAListFromREList(reList);
        language.setDFAList(dfaList);
        //构造CFG
        //CFG cfg = new CFG(productionStrList);
        //构造各解析表

        //保存language到缓存
        mLanguageCache.saveToCache(language);
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
        return new Compiler(language, config);
    }
}
