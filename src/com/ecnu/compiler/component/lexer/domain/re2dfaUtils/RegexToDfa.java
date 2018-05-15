package com.ecnu.compiler.component.lexer.domain.re2dfaUtils;

import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;

import java.util.*;

public class RegexToDfa {

    private static Set<Integer>[] followPos;
    private static Node root;
    private static List<DfaState> DStates;

    //输入的re会使用到的字符集
    private static Set<String> input = new HashSet<String>();

    //每个字符对应一个编号(重复的字符编号不同)
    //使用String而不是char是为了处理输入中含有转义字符(如'\*')的情况
    private static HashMap<Integer, String> symbNum;

//    public static void main(String[] args) {
//        initialize();
//    }

    public static void initialize(String expression) {
//        Scanner in = new Scanner(System.in);
        //allocating
        DStates = new ArrayList<>();
        input = new HashSet<>();

//        String regex = getRegex(in);
        String regex = expression;
        getSymbols(regex);

        //把RE作为参数传给语法树的构造方法，生成其对应的语法树
        SyntaxTree st = new SyntaxTree(regex);
        //语法树的根
        root = st.getRoot();
        //语法树的followPos
        followPos = st.getFollowPos();

        /**
         * creating the DFA using the syntax tree were created upside and
         * returning the start state of the resulted DFA
         */
        DfaState q0 = createDFA();
        DfaTraversal dfat = new DfaTraversal(q0, input);
        
//        String str = getStr(in);
//        boolean acc = false;
//        for (char c : str.toCharArray()) {
//            if (dfat.setCharacter(c)) {
//                acc = dfat.traverse();
//            } else {
//                System.out.println("WRONG CHARACTER!");
//                System.exit(0);
//            }
//        }
//        if (acc) {
//            System.out.println((char) 27 + "[32m" + "Accepted!");
//        } else {
//            System.out.println((char) 27 + "[31m" + "Rejected!");
//        }
//        in.close();
    }

    private static String getRegex(Scanner in) {
        System.out.print("Enter a regex: ");
        String regex = in.nextLine();
        return regex + "#";
    }

    private static void getSymbols(String regex) {
        //op是特殊符号的集合(如*号)
        Character[] ch = {'(', ')', '*', '|', '&', '.', '\\', '[', ']', '+'};
        Set<Character> op = new HashSet<>(Arrays.asList(ch));

        input = new HashSet<>();
        symbNum = new HashMap<>();
        int num = 1;
        for (int i = 0; i < regex.length(); i++) {
            char charAt = regex.charAt(i);

            //对于输入的特殊符号(如*号)，其前面如果有'\'则视为普通的输入符号
            if (op.contains(charAt)) {
                if (i - 1 >= 0 && regex.charAt(i - 1) == '\\') {
                    input.add("\\" + charAt);
                    symbNum.put(num++, "\\" + charAt);
                }
            } else {
                input.add("" + charAt);
                symbNum.put(num++, "" + charAt);
            }
        }
    }

    private static DfaState createDFA() {
        int id = 0;
        Set<Integer> firstpos_n0 = root.getFirstPos();

        DfaState q0 = new DfaState(id++);
        q0.addAllToName(firstpos_n0);
        if (q0.getName().contains(followPos.length)) {
            q0.setAccepted();
        }
        DStates.clear();
        DStates.add(q0);

        while (true) {
            boolean exit = true;
            DfaState s = null;
            for (DfaState state : DStates) {
                if (!state.getMarked()) {
                    exit = false;
                    s = state;
                }
            }
            if (exit) {
                break;
            }

            if (s.getMarked()) {
                continue;
            }
            //标记状态s
            s.setMarked(true);
            Set<Integer> name = s.getName();
            for (String a : input) {
                Set<Integer> U = new HashSet<>();
                for (int p : name) {
                    if (symbNum.get(p).equals(a)) {
                        U.addAll(followPos[p - 1]);
                    }
                }
                boolean flag = false;
                DfaState tmp = null;
                for (DfaState state : DStates) {
                    if (state.getName().equals(U)) {
                        tmp = state;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    DfaState q = new DfaState(id++);
                    q.addAllToName(U);
                    if (U.contains(followPos.length)) {
                        q.setAccepted();
                    }
                    DStates.add(q);
                    tmp = q;
                }
                s.addMove(a, tmp);
            }
        }
        for(DfaState state : DStates) {
            printState(state);
        }
        return q0;
    }

    private static void printState(DfaState state) {
        System.out.println(state);
    }

    private static String getStr(Scanner in) {
        System.out.print("Enter a string: ");
        String str;
        str = in.nextLine();
        return str;
    }

    public static DFA getDFA() {
        DFA dfa = new DFA();
        dfa.setStateList(DStates);
        clean(dfa);
        setEdgeList(dfa);
        setStartAndEnd(dfa);
        return dfa;
    }

    private static void setEdgeList(DFA dfa) {
        for(DfaState s : dfa.getDfaStateList()) {
            HashMap<String, DfaState> move = s.getAllMoves();
            Iterator it = move.entrySet().iterator();
            List<Edge> edgeList = new ArrayList<>();
            while(it.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) it.next();
                String keyString = (String) entry.getKey();
                char[] charArray = keyString.toCharArray();
                char key = charArray[0];
                DfaState val = (DfaState) entry.getValue();
                Edge edge = new Edge(s, val, key);
                edgeList.add(edge);
            }
            s.setEdgeList(edgeList);
        }
    }

    private static void clean(DFA dfa) {
        List<DfaState> stateList = dfa.getDfaStateList();
        Set<DfaState> removeSet = new HashSet<>();
        Set<Integer> removeIntSet = new HashSet<>();
        for(DfaState s : stateList) {
            if(s.getName().size() == 0) {
                removeSet.add(s);
                removeIntSet.add(s.getId());
            }
        }
        stateList.removeAll(removeSet);
        dfa.setStateList(stateList);
        for(DfaState s : stateList) {
            HashMap<String, DfaState> move = s.getAllMoves();
            Iterator it = move.entrySet().iterator();
            while(it.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) it.next();
                String keyString = (String) entry.getKey();
                char[] charArray = keyString.toCharArray();
                char key = charArray[0];
                if (key == '#') {
                    it.remove();
                    continue;
                }
                int val = ((DfaState) entry.getValue()).getId();
                if (removeIntSet.contains(val)) {
                    it.remove();
                }
            }
        }
    }

    private static void setStartAndEnd(DFA dfa) {
        List<DfaState> endStateList = new ArrayList<>();
        for(DfaState s : dfa.getDfaStateList()) {
            if(s.getId() == 0) {
                dfa.setStartState(s);
            }
            if(s.getAccepted()) {
                endStateList.add(s);
            }
        }
        dfa.setEndStateList(endStateList);
    }

}
