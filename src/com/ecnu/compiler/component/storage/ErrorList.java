package com.ecnu.compiler.component.storage;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import com.ecnu.compiler.component.storage.domain.ErrorMsg;
import com.ecnu.compiler.constant.StatusCode;

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

    public List<ErrorMsg> getErrorMsgList() {
        return errorMsgList;
    }

    /**
     * 获取最新的一个Error
     * @return
     */
    public ErrorMsg getLastError(){
        if(errorMsgList.size()==0){ return null; }
        return errorMsgList.get(errorMsgList.size()-1);
    }

    /**
     * 获取最新Error且从列表中删除他
     */
    public ErrorMsg handleError(){
        if (errorMsgList.size() <= 0)
            return null;
        ErrorMsg errorMsg = errorMsgList.get(errorMsgList.size() - 1);
        errorMsgList.remove(errorMsgList.size() -1);
        return errorMsg;
    }

    //打印所有错误并清空队列
    public void printAllErrorAndClear(){
        for (int i = errorMsgList.size() - 1; i >= 0; i--) {
            ErrorMsg errorMsg = errorMsgList.get(i);
            System.out.println(errorMsg.getStatusCode().getText() + ": " + errorMsg.getMsg());
        }
        errorMsgList.clear();
    }

    //添加一个错误信息
    public void addErrorMsg(String msg, StatusCode statusCode){
        addError(new ErrorMsg(-1, msg, statusCode));
    }

    /**
     * 添加一个Error信息
     * @param errorMsg
     */
    public void addError(ErrorMsg errorMsg){
        errorMsgList.add(errorMsg);
    }
}
