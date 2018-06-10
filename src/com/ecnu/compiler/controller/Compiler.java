package com.ecnu.compiler.controller;

import com.ecnu.compiler.component.CacheManager.Language;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import com.ecnu.compiler.constant.Config;
import com.ecnu.compiler.constant.Constants;
import com.ecnu.compiler.constant.StatusCode;

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
    //运行时间表
    private TimeHolder mTimeHolder;

    /**
     * 根据传入语言构建编译器
     */
    public Compiler(Language language, Config config){
        mStatus = StatusCode.STAGE_INIT;
        mErrorList = new ErrorList();
        mTimeHolder = new TimeHolder();
        mController = createController(language, config, mErrorList, mTimeHolder);
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

    /**
     * 获取时间表
     * @return 时间表
     */
    public TimeHolder getTimeHolder() {
        return mTimeHolder;
    }

    /**
     * 获取错误列表
     * @return 错误列表
     */
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
    private BaseController createController(Language language, Config config, ErrorList errorList, TimeHolder timeHolder){
        return new BaseController(language, config, errorList, timeHolder);
    }

    public class TimeHolder {
        //预处理时间
        private long preprocessorTime;
        //词法分析时间
        private long lexerTime;
        //语法分析时间
        private long parserTime;

        public long getPreprocessorTime() {
            return preprocessorTime;
        }

        void setPreprocessorTime(long preprocessorTime) {
            this.preprocessorTime = preprocessorTime;
        }

        public long getLexerTime() {
            return lexerTime;
        }

        void setLexerTime(long lexerTime) {
            this.lexerTime = lexerTime;
        }

        public long getParserTime() {
            return parserTime;
        }

        void setParserTime(long parserTime) {
            this.parserTime = parserTime;
        }
    }

}
