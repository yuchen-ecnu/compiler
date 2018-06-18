package com.ecnu.compiler.component.semantic.domain;

import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.constant.StatusCode;
import javafx.util.Pair;

import java.util.*;

public class AG {
    //每个产生式对应的Action的位置与内容,位置从0开始，表示对应下标字符的前面一个
    private Map<Integer, List<Pair<Integer, ActionSymbol>>> mActionPositionMap;
    //是否成功构造
    private boolean mIsSuccessfulBuild;

    /**
     * 构造函数，需要传入CFG的列表
     *
     * @param agProductionStrList AG对应产生式列表
     */
    public AG(List<String> agProductionStrList, Map<String, String> actionMap, ErrorList errorList) {
        mIsSuccessfulBuild = true;
        if (agProductionStrList == null ||actionMap == null){
            mIsSuccessfulBuild = false;
            errorList.addErrorMsg("构造AG失败，有参数为null", StatusCode.ERROR_INIT);
            return;
        }
        CFG cfg = new CFG(agProductionStrList, errorList);
        if (!cfg.isSucessfulBuild()){
            mIsSuccessfulBuild = false;
            errorList.addErrorMsg("构造AG失败，产生式有错误", StatusCode.ERROR_INIT);
        }
        mActionPositionMap = new HashMap<>();
        List<Production> productionList = cfg.getAllProductions();
        for (Production production : productionList){
            List<Pair<Integer, ActionSymbol>> tableLine = new ArrayList<>();
            List<Symbol> right = production.getRight();
            int curSymbolIndex = 0;
            for (Symbol symbol : right){
                String action = actionMap.get(symbol.getName());
                if (action != null){
                    tableLine.add(new Pair<>(curSymbolIndex, new ActionSymbol(symbol.getName(), action)));
                }
                curSymbolIndex++;
            }

            mActionPositionMap.put(production.getId(), tableLine);
        }

    }

    public List<Pair<Integer, ActionSymbol>> getActionPosListByProductionId(int productionId){
        return mActionPositionMap.get(productionId);
    }

    public boolean isSuccessfulBuild() {
        return mIsSuccessfulBuild;
    }
}
