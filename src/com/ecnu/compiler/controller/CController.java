package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.base.BaseController;

import java.io.File;

/**
 * C语言编译控制器
 *
 * @author Michael Chen
 * @date 2018-05-01 16:28
 */
public class CController extends BaseController{

    public CController(File file, Config config) {
        super(file, config);
        this.preprocessor = new Preprocessor();
        //配置文件出错
        if(this.lexer == null || this.parser == null){
            this.status = StatusCode.ERROR_INIT;
        }else{
            this.status = StatusCode.RUNNING;
        }
    }

    @Override
    public StatusCode next() {
        switch(config.getExecuteType()){
            //全部执行
            case Constants.EXECUTE_IN_ONE_STEP:
                break;
            //单阶段执行
            case Constants.EXECUTE_STAGE_BY_STAGE:
                break;
            //单步执行
            case Constants.EXECUTE_STEP_BY_STEP:
                break;
            default:
                return StatusCode.ERROR;
        }
        return StatusCode.SUCCESS;
    }
}
