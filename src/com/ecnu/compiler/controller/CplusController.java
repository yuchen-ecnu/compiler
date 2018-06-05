package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
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

    public CplusController(Language language, Config config, ErrorList errorList) {
        super(language, config, errorList);
    }

    @Override
    protected Preprocessor createPreprocessor() {
        return new Preprocessor(Constants.CPP_COMMENT);
    }
}
