package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.preprocessor.Preprocessor;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;
import com.ecnu.compiler.controller.base.BaseController;

/**
 * C语言编译控制器
 *
 * @author Michael Chen
 * @date 2018-05-01 16:28
 */
public class CController extends BaseController{

    public CController(Language language, Config config, ErrorList errorList) {
        super(language, config, errorList);
    }

    @Override
    protected Preprocessor createPreprocessor() {
        return new Preprocessor(Constants.CPP_COMMENT);
    }
}
