package com.ecnu;

import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.CController;
import com.ecnu.compiler.controller.CplusController;
import com.ecnu.compiler.controller.JavaController;
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
    private BaseController controller;
    private StatusCode status;

    /**
     * 创建编译器程序
     * @param file 输入文件句柄
     * @param config 可配置参数类（参见使用说明）
     */
    public Compiler(File file, Config config) {

        //创建对应语言控制器
        this.controller = createController(file, config);
        if(controller == null){
            this.status = StatusCode.ERROR;
        }
        this.status = StatusCode.RUNNING;
    }

    /**
     * 根据配置文件执行一步
     * @return 编译器当前状态码（参见状态说明文档）
     */
    public StatusCode next(){
        //检查是否出现异常 或 已结束
        if(controller == null || status != StatusCode.RUNNING){
            return StatusCode.ERROR;
        }
        //执行一步，并更新状态
        this.status = controller.next();
        return this.status;
    }

    public StatusCode getStatus() {
        return status;
    }

    /**
     * 创建编译控制器
     * @param config 配置文件
     * @return 对应类型的控制器
     */
    private BaseController createController(File file, Config config){
        switch(config.getCompileLanguage()){
            case Constants.LANGUAGE_JAVA:
                return new JavaController(file,config);
            case Constants.LANGUAGE_C:
                return new CController(file,config);
            case Constants.LANGUAGE_CPLUS:
                return new CplusController(file,config);
            default:
                return null;
        }

    }
}
