package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.storage.ErrorList;
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
        createController(language, config, mErrorList);
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
        //检查是否出现异常 或 已结束
        if(mController == null || mStatus != StatusCode.RUNNING){
            return StatusCode.ERROR;
        }
        //执行一步，并更新状态
        this.mStatus = mController.next();
        return this.mStatus;
    }

    public StatusCode getStatus() {
        return mStatus;
    }

    /**
     * 创建编译控制器
     * @param config 配置文件
     * @return 对应类型的控制器
     */
    private BaseController createController(Language language, Config config, ErrorList errorList){
        switch(config.getCompileLanguage()){
            case Constants.LANGUAGE_JAVA:
                return new JavaController(language,config, errorList);
            case Constants.LANGUAGE_C:
                return new CController(language,config, errorList);
            case Constants.LANGUAGE_CPLUS:
                return new CplusController(language,config, errorList);
            default:
                //todo 配置错误
                return null;
        }

    }
}
