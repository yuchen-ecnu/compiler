package com.ecnu.compiler.component.parser.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Tree Diagram
 *
 * @author Michael Chen
 * @date 2018-04-24 13:13
 */
public class TD {
    //保存一个根节点
    private TNode mRoot;

    public TD() {}

    public TD(TNode root) {
        this.mRoot = root;
    }

    public TNode getRoot() {
        return mRoot;
    }

    public void setRoot(TNode root) {
        this.mRoot = root;
    }

    public void DFS(DoOnNode doOnNode){
        DFSRecursive(mRoot, doOnNode);
    }


    static public interface DoOnNode{
        void doOnNode(TNode tNode);
    }
    //树节点
    static public class TNode{
        //对应产生式ID，如果是叶子节点，那么为默认的-1；
        private int mProductionId = -1;
        //节点内容
        private String content;
        //孩子节点
        private List<TNode> children;

        public TNode(String content) {
            this.content = content;
            this.children = new ArrayList<>();
            this.matched = false;
        }

        private boolean matched;

        public TNode() {
            this.content = null;
            this.children = new ArrayList<>();
            this.matched = false;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<TNode> getChildren() {
            return children;
        }

        public void setChildren(List<TNode> children) {
            this.children = children;
        }

        public void addChild(TNode child){
            children.add(child);
        }

        public int getProductionId() {
            return mProductionId;
        }

        public void setProductionId(int productionId) {
            mProductionId = productionId;
        }

        public void reverseChildren(){
            List<TNode> newChildren = new ArrayList<>();
            for (int i = this.children.size() - 1; i >= 0; i--) {
                newChildren.add(this.children.get(i));
            }
            this.children = newChildren;
        }

        public boolean isMatched() {
            return matched;
        }

        public void setMatched(boolean matched) {
            this.matched = matched;
        }

        @Override
        public String toString() {
            return content;
        }
    }
    
    static public class ActionNode extends TNode{
        private String mAction;

        public ActionNode(String content, String action) {
            super(content);
            mAction = action;
        }

        public String getAction() {
            return mAction;
        }

        @Override
        public String toString() {
            return getContent() + " : {" + mAction + "}";
        }
    }

    public static void printTree(TD tree) {
        Stack<TNode> stack = new Stack<>();
        System.out.println("Root:" + tree.getRoot().getContent());
        System.out.println("-----------");
        stack.push(tree.getRoot());
        while(!stack.isEmpty()) {
            TD.TNode curNode = stack.pop();
            System.out.println("Cur:" + curNode.getContent());
            if (!curNode.getChildren().isEmpty()) {
                List<TD.TNode> children = curNode.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }
    }

    private void DFSRecursive(TNode tNode, DoOnNode doOnNode){
        doOnNode.doOnNode(tNode);
        List<TNode> children = tNode.getChildren();
        if (children != null && children.size() > 0){
            for (TNode child : children){
                DFSRecursive(child, doOnNode);
            }
        }
    }
}
