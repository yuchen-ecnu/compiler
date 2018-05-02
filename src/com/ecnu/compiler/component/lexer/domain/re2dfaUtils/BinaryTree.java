package com.ecnu.compiler.component.lexer.domain.re2dfaUtils;

import java.util.*;

class BinaryTree {
    
    /*
        ***
            (a|b)*a => creating binary syntax tree:
                                .
                               / \
                              *   a
                             /
                            |
                           / \
                          a   b
        ***
    */

    private int leafNodeID = 0;
    
    // Stacks for symbol nodes and operators
    private Stack<Node> stackNode = new Stack<>();
    private Stack<Character> operator = new Stack<Character>();

    // Set of inputs
    private Set<Character> input = new HashSet<Character>();
    private ArrayList<Character> op = new ArrayList<>();

    //根据re生成语法树，返回语法树的root
    public Node generateTree(String regular) {

        Character[] ops = {'*', '|', '&'};
        op.addAll(Arrays.asList(ops));

        //包含A-Z,a-z的数组
        Character ch[] = new Character[26 + 26];
        for (int i = 65; i <= 90; i++) {
            ch[i - 65] = (char) i;
            ch[i - 65 + 26] = (char) (i + 32);
        }
        Character integer[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Character others[] = {'#','\\', '=', '_', '.', '*', '/', '+', '-', ' ', '(', ')'};
        input.addAll(Arrays.asList(ch));
        input.addAll(Arrays.asList(integer));
        input.addAll(Arrays.asList(others));

        //在re中适当位置加入"&"
        regular = AddConcat(regular);
        //添加了&以后的re，调试用
        System.out.println("New RE: " + regular);
        System.out.println();
        
        //清空stacks
        stackNode.clear();
        operator.clear();

        //输入中含有'\('、'\*'一类的转义字符时，为true
        boolean isSymbol = false;

        for (int i = 0; i < regular.length(); i++) {

            //表明下一个字符会是符号
            if (regular.charAt(i) == '\\') {
                isSymbol = true;
                continue;
            }
            //当前字符是合法输入(是符号或合法的字母、数字)
            if (isSymbol || isInputCharacter(regular.charAt(i))) {
                if (isSymbol) {
                    //当前为(、*之类的符号时，在前面加上"\"加入stackNode
                    pushStack("\\"+Character.toString(regular.charAt(i)));
                }
                else{
                    //否则为a、b之类的字母或数字，直接加入stackNode
                    pushStack(Character.toString(regular.charAt(i)));
                }
                isSymbol = false;
            } else if (operator.isEmpty()) {
                operator.push(regular.charAt(i));

            } else if (regular.charAt(i) == '(') {
                operator.push(regular.charAt(i));

            } else if (regular.charAt(i) == ')') {
                while (operator.get(operator.size() - 1) != '(') {
                    doOperation();
                }

                //将左括号'('pop出去
                operator.pop();

            } else {
                while (!operator.isEmpty()
                        && Priority(regular.charAt(i), operator.get(operator.size() - 1))) {
                    doOperation();
                }
                operator.push(regular.charAt(i));
            }
        }

        //清除stack中的剩余元素
        while (!operator.isEmpty()) {
            doOperation();
        }

        //得到complete Tree
        Node completeTree = stackNode.pop();
        return completeTree;
    }

    private boolean Priority(char first, Character second) {
        if (first == second) {
            return true;
        }
        if (first == '*') {
            return false;
        }
        if (second == '*') {
            return true;
        }
        if (first == '&') {
            return false;
        }
        if (second == '&') {
            return true;
        }
        if (first == '|') {
            return false;
        }
        return true;
    }

    //根据stackNode栈顶元素，进行不同的操作
    private void doOperation() {
        if (this.operator.size() > 0) {
            char charAt = operator.pop();

            switch (charAt) {
                case ('|'):
                    union();
                    break;

                case ('&'):
                    concatenation();
                    break;

                case ('*'):
                    star();
                    break;

                default:
                    System.out.println(">>" + charAt);
                    System.out.println("Unknown Symbol!");
                    System.exit(1);
                    break;
            }
        }
    }

    // Do the star operation
    private void star() {
        // Retrieve top Node from Stack
        Node node = stackNode.pop();

        Node root = new Node("*");
        root.setLeft(node);
        root.setRight(null);
        node.setParent(root);

        // Put node back in the stackNode
        stackNode.push(root);
    }

    // Do the concatenation operation
    private void concatenation() {
        // retrieve node 1 and 2 from stackNode
        Node node2 = stackNode.pop();
        Node node1 = stackNode.pop();

        Node root = new Node("&");
        root.setLeft(node1);
        root.setRight(node2);
        node1.setParent(root);
        node2.setParent(root);

        // Put node back to stackNode
        stackNode.push(root);
    }

    // Makes union of sub Node 1 with sub Node 2
    private void union() {
        // Load two Node in stack into variables
        Node node2 = stackNode.pop();
        Node node1 = stackNode.pop();

        Node root = new Node("|");
        root.setLeft(node1);
        root.setRight(node2);
        node1.setParent(root);
        node2.setParent(root);

        // Put Node back to stack
        stackNode.push(root);
    }

    //创建叶子结点，并加入stackNode
    private void pushStack(String symbol) {
        Node node = new LeafNode(symbol, ++leafNodeID);
        node.setLeft(null);
        node.setRight(null);

        stackNode.push(node);
    }

    // add "." when is concatenation between to symbols that: "." -> "&"
    // concatenates to each other
    private String AddConcat(String regular) {
        String newRegular = "";

        for (int i = 0; i < regular.length() - 1; i++) {
            /*
             *#  consider a , b are characters in the Σ
             *#  and the set: {'(', ')', '*', '+', '&', '|'} are the operators
             *#  then, if '&' is the concat symbol, we have to concatenate such expressions:
             *#  a & b
             *#  a & (
             *#  ) & a
             *#  * & a
             *#  * & (
             *#  ) & (
             */
            if (regular.charAt(i) == '\\' && isInputCharacter(regular.charAt(i + 1))) {
                newRegular += regular.charAt(i);
            } else if (regular.charAt(i) == '\\' && regular.charAt(i + 1) == '(') {
                newRegular += regular.charAt(i);
            } else if ((isInputCharacter(regular.charAt(i)) || (regular.charAt(i) == '(' && i > 0 && regular.charAt(i - 1) == '\\')) && isInputCharacter(regular.charAt(i + 1))) {
                newRegular += regular.charAt(i) + "&";

            } else if ((isInputCharacter(regular.charAt(i)) || (regular.charAt(i) == '(' && i > 0 && regular.charAt(i - 1) == '\\')) && regular.charAt(i + 1) == '(') {
                newRegular += regular.charAt(i) + "&";

            } else if (regular.charAt(i) == ')' && isInputCharacter(regular.charAt(i + 1))) {
                newRegular += regular.charAt(i) + "&";

            } else if (regular.charAt(i) == '*' && isInputCharacter(regular.charAt(i + 1))) {
                newRegular += regular.charAt(i) + "&";

            } else if (regular.charAt(i) == '*' && regular.charAt(i + 1) == '(') {
                newRegular += regular.charAt(i) + "&";

            } else if (regular.charAt(i) == ')' && regular.charAt(i + 1) == '(') {
                newRegular += regular.charAt(i) + "&";

            } else {
                newRegular += regular.charAt(i);
            }

        }
        newRegular += regular.charAt(regular.length() - 1);
        return newRegular;
    }

    //是合法的输入，且不是符号（例如字母、数字），则返回true
    private boolean isInputCharacter(char charAt) {

        if (op.contains(charAt)) {
            return false;
        }
        for (Character c : input) {
            if ((char) c == charAt && charAt != '(' && charAt != ')') {
                return true;
            }
        }
        return false;
    }
    
    /* 前序遍历输出整棵树，用于调试 */
    public void printInorder(Node node) {
        if (node == null) {
            return;
        }
        printInorder(node.getLeft());
        System.out.print(node.getSymbol() + " ");
        printInorder(node.getRight());
    }

    //获取叶子结点数目
    public int getNumberOfLeafs(){
        return leafNodeID;
    }

}
