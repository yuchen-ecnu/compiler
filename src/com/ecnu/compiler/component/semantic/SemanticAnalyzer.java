package com.ecnu.compiler.component.semantic;

import com.ecnu.compiler.component.parser.base.Parser;
import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.ParsingTable.ParsingTable;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
import com.ecnu.compiler.component.parser.domain.TD;
import com.ecnu.compiler.component.semantic.domain.AG;
import com.ecnu.compiler.component.semantic.domain.ActionSymbol;
import com.ecnu.compiler.component.storage.ErrorList;
import com.ecnu.compiler.component.storage.SymbolTable;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 语义分析器
 *
 * @author Michael Chen
 * @date 2018-05-01 20:08
 */
public class SemanticAnalyzer {
    //AG
    private AG mAG;
    //错误列表
    private ErrorList mErrorList;

    public SemanticAnalyzer(AG ag, ErrorList errorList) {
        mAG = ag;
        mErrorList = errorList;
    }

    public List<String> buildAttrubuteTree(TD syntaxTree){
        //在原语法树的基础上构造注释语法树
        syntaxTree.DFS(tNode -> {
            if (tNode.getProductionId() > 0){
                //如果是非叶子节点
                List<Pair<Integer, ActionSymbol>> actionPosList = mAG.getActionPosListByProductionId(tNode.getProductionId());
                List<TD.TNode> children = tNode.getChildren();
                for (Pair<Integer, ActionSymbol> actionPos : actionPosList){
                    ActionSymbol actionSymbol = actionPos.getValue();
                    TD.ActionNode actionNode = new TD.ActionNode(actionSymbol.getName(), actionSymbol.getAction());
                    children.add(actionPos.getKey(), actionNode);
                }
            }
        });

        return getActionList(syntaxTree);
    }

    private List<String> getActionList(TD syntaxTree){
        ArrayList<String> actionList = new ArrayList<>();
        //todo 深度中序遍历获取所有的Action
        syntaxTree.DFS(tNode -> {
            if (tNode instanceof TD.ActionNode){
                actionList.add(((TD.ActionNode) tNode).getAction());
            }
        });

        return actionList;
    }

}
