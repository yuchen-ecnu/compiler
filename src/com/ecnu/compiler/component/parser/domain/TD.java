package com.ecnu.compiler.component.parser.domain;

import java.util.List;

/**
 * Tree Diagram
 *
 * @author Michael Chen
 * @date 2018-04-24 13:13
 */
public class TD {
    //保存一个根节点
    private TNode<String> root;

    public TD(TNode<String> root) {
        this.root = root;
    }

    public TNode<String> getRoot() {
        return root;
    }

    public void setRoot(TNode<String> root) {
        this.root = root;
    }

    //树节点
    static public class TNode<NodeType>{
        //节点内容
        private NodeType content;
        //孩子节点
        private List<TNode<NodeType>> children;

        public TNode(NodeType content) {
            this.content = content;
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
    }
}
