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
    private TNode<String> root;

    public TD() {

    }

    public TD(TNode<String> root) {
        this.root = root;
    }

    public TNode<String> getRoot() {
        return root;
    }

    public void setRoot(TNode<String> root) {
        this.root = root;
    }


    public static void printTree(TD tree) {
        Stack<TNode> stack = new Stack<>();
        System.out.println("Root:" + tree.getRoot().getContent());
        System.out.println("-----------");
        stack.push(tree.getRoot());
        while(!stack.isEmpty()) {
            TD.TNode<String> curNode = stack.pop();
            System.out.println("Cur:" + curNode.getContent());
            if (!curNode.getChildren().isEmpty()) {
                List<TD.TNode<String>> children = curNode.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }
    }

    //树节点
    static public class TNode<NodeType>{
        //节点内容
        private NodeType content;
        //孩子节点
        private List<TNode<NodeType>> children;

        public TNode(NodeType content) {
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

        public NodeType getContent() {
            return content;
        }

        public void setContent(NodeType content) {
            this.content = content;
        }

        public List<TNode<NodeType>> getChildren() {
            return children;
        }

        public void setChildren(List<TNode<NodeType>> children) {
            this.children = children;
        }

        public void addChild(TNode<NodeType> child){
            children.add(child);
        }

        public void reverseChildren(){
            List<TNode<NodeType>> newChildren = new ArrayList<>();
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
    }
}
