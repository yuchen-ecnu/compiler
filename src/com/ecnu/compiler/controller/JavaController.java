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
 * JAVA 编译控制器
 *
 * @author Michael Chen
 * @date 2018-05-01 16:29
 */
public class JavaController extends BaseController {

    public JavaController(Language language, Config config, ErrorList errorList) {
        super(language, config, errorList);
    }

    @Override
    protected Preprocessor createPreprocessor() {
        return new Preprocessor(Constants.JAVA_COMMENT);
    }
}
