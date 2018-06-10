package com.ecnu.compiler.component.storage.domain;

/**
 * @author Michael Chen
 * @date 2018-05-01 15:03
 */
public class Token {
    private String mType;
    private String mStr;
    private int mRowNumber;
    private int mColPosition;

    public Token(String type, String stringText) {
        this.mType = type;
        mStr = stringText;
    }

    public Token(String type) {
        mType = type;
    }



    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getStr() {
        return mStr;
    }

    public void setStr(String str) {
        mStr = str;
    }

    public int getRowNumber() {
        return mRowNumber;
    }

    public void setRowNumber(int rowNumber) {
        mRowNumber = rowNumber;
    }

    public int getColPosition() {
        return mColPosition;
    }

    public void setColPosition(int colPosition) {
        mColPosition = colPosition;
    }
}
