package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.base.BaseController;

import java.io.File;

/**
 * 编译器  --定义对外的所有接口
 * （最终作为Jar包导入WEB程序中）
 *
 * @author Michael Chen
 * @date 2018/05/01
 */
public class Compiler {
    //进度控制器
    private BaseController mController;
    //当前状态码
    private StatusCode mStatus;
    //错误信息列表
    private ErrorList mErrorList;

    /**
     * 根据传入语言构建编译器
     */
    public Compiler(Language language, Config config){
        mStatus = StatusCode.STAGE_INIT;
        mErrorList = new ErrorList();
        mController = createController(language, config, mErrorList);
        mStatus = StatusCode.RUNNING;
    }

    /**
     * 使用编译器进行编译
     */
    public StatusCode prepare(String text){
        mStatus = mController.prepare(text);
        return mStatus;
    }

    /**
     * 根据配置文件执行一步
     * @return 编译器当前状态码（参见状态说明文档）
     */
    public StatusCode next(){
        //执行一步，并更新状态
        mStatus = mController.next();
        return mStatus;
    }

    public StatusCode getStatus() {
        return mStatus;
    }

    /**
     * 获得生成的符号表
     * @return 符号表，可能为null
     */
    public SymbolTable getSymbolTable(){
        return mController.getSymbolTable();
    }

    public ErrorList getErrorList() {
        return mErrorList;
    }

    /**
     * 创建编译进程控制器
     * @param language 语言信息
     * @param config 配置信息
     * @param errorList 错误列表
     * @return 相应的控制器
     */
    private BaseController createController(Language language, Config config, ErrorList errorList){
        switch(language.getBaseLanguage()){
            case Constants.LANGUAGE_JAVA:
                return new JavaController(language,config, errorList);
            case Constants.LANGUAGE_C:
                return new CController(language,config, errorList);
            case Constants.LANGUAGE_CPLUS:
                return new CplusController(language,config, errorList);
            default:
                //todo 语言不支持
                return null;
        }
    }

}
