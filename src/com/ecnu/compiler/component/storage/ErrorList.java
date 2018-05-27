package com.ecnu.compiler.component.storage;

import java.util.ArrayList;
import java.util.List;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;

/**
 * 异常信息列表
 *
 * @author Michael Chen
 * @date 2018-05-01 21:28
 */
public class ErrorList {
    private List<ErrorMsg> errorMsgList;

    public ErrorList() {
        this.errorMsgList = new ArrayList<>();
    }

    /**
     * 获取最新的一个Error
     * @return
     */
    public ErrorMsg getLastError(){
        return errorMsgList.get(errorMsgList.size()-1);
    }

    /**
     * 获取最新Error且从列表中删除他
     */
    public ErrorMsg handleError(){
        ErrorMsg errorMsg = errorMsgList.get(errorMsgList.size() - 1);
        errorMsgList.remove(errorMsgList.size() -1);
        return errorMsg;
    }

    /**
     * 添加一个Error信息
     * @param errorMsg
     */
    public void addError(ErrorMsg errorMsg){
        errorMsgList.add(errorMsg);
    }
}
