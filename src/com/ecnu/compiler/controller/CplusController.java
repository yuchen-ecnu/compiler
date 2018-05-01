package com.ecnu.compiler.controller;

import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.base.BaseController;

import java.io.File;

/**
 * C++编译控制器
 *
 * @author Michael Chen
 * @date 2018-05-01 16:27
 */
public class CplusController extends BaseController {


    public CplusController(File file, Config config) {
        super(file, config);
    }

    @Override
    public StatusCode next() {
        return StatusCode.SUCCESS;
    }
}
