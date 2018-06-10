package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.util.*;

/**
 * 确定性有限状态自动机
 * @author Michael Chen
 */
public class DFA extends Graph {

    static public ArrayList<DFA> buildDFAListFromREList(List<RE> reList){
        ArrayList<DFA> dfaList = new ArrayList<>();
        //使用直接构造
        for (RE re : reList){
            dfaList.add(re.getDFAIndirect());
        }

        //最小化状态
        for (DFA dfa : dfaList){
            DFA2MinDFA(dfa);
        }
        return dfaList;
    }

    private String mName;

    private List<State> mEndStateList;

    private State mStartState;

    public List<List<TransitMat>> mStateTransitionMat;

    public DFA() {
        this.mEndStateList = new ArrayList<>();
    }

    public DFA(List<DfaState> dfaStateList,List<List<TransitMat>> stateTransitionMat) {
        this.setDfaStateList(dfaStateList);
        this.mStateTransitionMat = stateTransitionMat;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public State getStartState() {
        return mStartState;
    }

    public void setStartState(State startState) {
        this.mStartState = startState;
    }

    public List<DfaState> getDfaStateList() {
        List<DfaState> list = new ArrayList<>();
        for (State state : super.getStateList()){
            list.add((DfaState)state);
        }
        return list;
    }

    public void setDfaStateList(List<DfaState> dfaStateList) {
        List<State> list = new ArrayList<>(dfaStateList);
        super.setStateList(list);
    }

    public void addOnlyOneEndStateList(List<DfaState> endStateList) {
        mEndStateList = new ArrayList<>(endStateList);
    }

    public State take(char input){
        return null;
    }


    public void print() {
        String states = "States:\n";
        String edges = "Edges:\n";
        String endStates = "End States:\n";
        for(State s : getStateList()) {
            states += s.toString() + "\n";
            for(Edge e : s.getEdgeList()){
                edges += e.toStringForDfa();
            }
        }
        for(State s : mEndStateList) {
            endStates = endStates + s.getId() + "\n";
        }
        System.out.println(states);
        System.out.println(edges);
        System.out.println("Start State:\n" + mStartState.getId());
        System.out.println(endStates);
    }

    public int getMaxId(){
        int maxId=0;
        for(State s : this.stateList){
            if(s.getId()>maxId){
                maxId=s.getId();
            }
        }
        return maxId;
    }

    public State getStateById(int id) {
        for (State state : stateList) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public List<State> getEndStateList() {
        return mEndStateList;
    }

    public void setEndStateList(List<State> mEndStateList) {
        this.mEndStateList = mEndStateList;
    }

    public void addOnlyOneEndStateList(State endState) {
        //getEndStateList().get(0).isAccepted = false;
        endState.isAccepted = true;
        this.mEndStateList.add(endState);
    }

    /**
     * match
     * @param lexeme 希望匹配的源代码
     * @return 如果匹配成功，则返回匹配路径的列表；如果匹配失败，则返回NULL
     * @author Lucto
     */
    public List<Integer> match(String lexeme){
        //author: huge
        //结果路径
        List<Integer> resultPath = new ArrayList<>();
        //从开始状态开始
        State curentState = getStartState();
        //当前识别的字符index
        int i = 0;
        tag: while (i < lexeme.length() && !curentState.isAccepted){
            List<Edge> outEdge = curentState.getEdgeList();
            for (Edge edge : outEdge){
                if (edge.getWeight() == lexeme.charAt(i)){
                    resultPath.add(curentState.getId());
                    curentState = edge.getEndState();
                    i++;
                    continue tag;
                }
            }
            //运行到此则说明匹配失败
            return null;
        }
        //保证DFA到达结束状态且字符全部被识别
        if (i < lexeme.length() || !curentState.isAccepted){
            return null;
        }
        return resultPath;




        //author：Lucto
        /*
        if(lexeme == null) {
            return null;
        }
        List<Boolean> success = new ArrayList<>();
        success.add(false);
        List<Integer> path = new ArrayList<>();
        path.clear();
        char[] chars = lexeme.toCharArray();
        //开始匹配
        startMatch(chars,0,path, mStartState,success);
        //匹配成功
        if(success.get(0)){
            return path;
        }else{
            return null;
        }*/
    }

    /**
     * startMatch
     * @param chars 希望匹配的单词序列
     * @param cur 当前匹配到的单词
     * @author Lucto
     */
    private void startMatch(char[] chars,int cur,List<Integer> path,State currentState,List<Boolean> success){
        //已经遍历完chars
        if(cur >= chars.length) {
            //到达dfa的结束状态
            if(mEndStateList.contains(currentState)) {
                //表示已成功匹配
                path.add(currentState.getId());
                success.remove(0);
                success.add(true);
            }
        }else {
            List<Edge> edgeList = currentState.getEdgeList();
            if(!edgeList.isEmpty()) {
                for (Edge edge : edgeList) {
                    if (chars[cur] == edge.getWeight()) {
                        path.add(currentState.getId());
                        startMatch(chars, cur + 1, path, edge.getEndState(), success);
                        if (success.get(0)) {
                            return;
                        } else {
                            path.remove(path.size() - 1);
                        }
                    }
                }
            }
        }
    }

    private static class Group {
        List<State> states;
        int id;

        public Group(List<State> states, int id) {
            this.states = states;
            this.id = id;
        }

        public List<State> getStates() {
            return states;
        }

        public void setStates(List<State> states) {
            this.states = states;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static DFA DFA2MinDFA(DFA dfa) {
        Group nonEndGroup = new Group(dfa.getStateList(), 0);
        Set<Character> weightList = new HashSet<>();
        for (State s : nonEndGroup.getStates()) {
            List<Edge> edgeList = s.getEdgeList();
            for (Edge e : edgeList) {
                char weight = e.getWeight();
                weightList.add(weight);
            }
        }
        Group endGroup = new Group(dfa.getEndStateList(), 1);
        nonEndGroup.getStates().removeAll(endGroup.getStates());
        List<Group> groups = new ArrayList<>();
        groups.add(nonEndGroup);
        groups.add(endGroup);
        boolean changed = true;

        while(changed) {
            int originalSize = groups.size();

            Map<Character, Map<State, State>> charMap = new HashMap<>();
            for (Group group : groups) {
                if (group.getStates().size() == 1) continue;
                for (char c : weightList) {
                    Map<State, State> map = new HashMap<>();
                    for (State s : group.getStates()) {
                        List<Edge> edgeList = s.getEdgeList();
                        for (Edge e : edgeList) {
                            if (e.getWeight() == c) {
                                State state = e.getEndState();
                                map.put(s, state);
                                break;
                            }
                        }
                    }
                    charMap.put(c, map);
                }
            }

            if(charMap.size() == 0) {
                break;
            }

            Map<State, List<Integer>> groupMap = new HashMap<>();
            for (Group group : groups) {
                for (State s : group.getStates()) {
                    List<Integer> groupIdList = new ArrayList<>();
                    for (char c : weightList) {
                        State transState = charMap.get(c).get(s);
                        for (Group g : groups) {
                            if (g.getStates().contains(transState)) {
                                groupIdList.add(g.getId());
                                break;
                            }
                        }
                    }
                    groupMap.put(s, groupIdList);
                }
            }

            for (Map.Entry entry : charMap.entrySet()) {
                char c = (char) entry.getKey();
                System.out.println("Char:" + entry.getKey());
                for (Map.Entry e : ((Map<State, State>) entry.getValue()).entrySet()) {
                    System.out.println(e.getKey() + "----" + e.getValue());
                }
            }

            for (Map.Entry entry : groupMap.entrySet()) {
                State s = (State) entry.getKey();
                System.out.println("State:" + s + "----");
                List<Integer> list = (List<Integer>) entry.getValue();
                for (int i : list) {
                    System.out.print(i + " ");
                }
                System.out.println();
            }

            Map<List<Integer>, Group> listGroupMap = new HashMap<>();
            groups.clear();
            int id = 0;
            for (Map.Entry entry : groupMap.entrySet()) {
                boolean exist = false;
                boolean nullList = false;
                List<Integer> existList = null;
                State s = (State) entry.getKey();
                List<Integer> list = (List<Integer>) entry.getValue();
                if (list == null || list.size() == 0) {
                    nullList = true;
                }
                for (List<Integer> l : listGroupMap.keySet()) {
                    if (l.containsAll(list) && list.containsAll(l)) {
                        exist = true;
                        existList = l;
                        break;
                    }
                }
                if (!exist || nullList) {
                    List<State> stateList = new ArrayList<>();
                    stateList.add(s);
                    Group group = new Group(stateList, id++);
                    groups.add(group);
                    listGroupMap.put(list, group);
                } else {
                    Group group = listGroupMap.get(existList);
                    List<State> stateList = group.getStates();
                    stateList.add(s);
                }
            }

            for (Group g : groups) {
                System.out.println("Group Id:" + g.getId());
                System.out.println("States:");
                for (State s : g.getStates()) {
                    System.out.print(s.getId() + " ");
                }
                System.out.println();
            }

            int currentSize = groups.size();
            changed = (originalSize != currentSize);
        }

        DFA newDFA = new DFA();
        List<State> newStates = new ArrayList<>();
        List<State> removedStates = new ArrayList<>();
        Map<State, State> delegateMap = new HashMap<>();
        for (Group group : groups) {
            boolean first = true;
            State delegate = null;
            for (State s : group.getStates()) {
                if (first) {
                    newStates.add(s);
                    delegate = s;
                    first = false;
                } else {
                    removedStates.add(s);
                    delegateMap.put(s, delegate);
                }
            }
        }
        for (State s : newStates) {
            List<Edge> edgeList = s.getEdgeList();
            for (Edge e : edgeList) {
                if (removedStates.contains(e.getEndState())) {
                    e.setEndState(delegateMap.get(e.getEndState()));
                }
            }
        }
        newDFA.setStateList(newStates);

        State originalStartState = dfa.getStartState();
        if (removedStates.contains(originalStartState)) {
            newDFA.setStartState(delegateMap.get(originalStartState));
        } else {
            newDFA.setStartState(originalStartState);
        }

        List<State> originalEndStates = dfa.getEndStateList();
        List<State> newEndStates = new ArrayList<>();
        for (State s : originalEndStates) {
            if (removedStates.contains(s)) {
                newEndStates.add(delegateMap.get(s));
            } else {
                newEndStates.add(s);
            }
        }
        newDFA.setEndStateList(newEndStates);

        return newDFA;
    }


//    /**
//     * minimize DFA
//     *
//     * @param dfa DFA
//     * @return DFA
//     * @author Chen Jianing
//     **/
//
///*<<<<<<< HEAD
//    public static DFA DFA2MinDFA(DFA dfa) throws IOException {
//        dfa.stateTransitionMat = GetTransitMat(dfa);
//=======*/
////    public static DFA DFA2MinDFA(DFA dfa){
////        return dfa;
////        /*
//        //GetTransitMat(dfa);
//        dfa.mStateTransitionMat = GetTransitMat(dfa);
////>>>>>>> dev
//        //用于存放最小化的过程中产生的状态划分
//        List<List<State>> stateListsPartition = new ArrayList<List<State>>();
//
//        //phrase 1: 将化简前的DFA的状态分为非可接受状态和可接受状态两部分
//        List<State> nonTerminalStates = new ArrayList<State>();
//        List<State> copyOfOriginalState = CloneOfStateList(dfa.stateList);
////        System.out.println(copyOfOriginalState);
////        for(State state:dfa.mEndStateList){
////            System.out.print(state.getId());
////        }
//        for (State state : copyOfOriginalState) {
//            for(State endState : dfa.mEndStateList){
//                if(state.getId()!=endState.getId()) {
//                    nonTerminalStates.add(state);
//                }
//            }
//        }
//<<<<<<< HEAD
//        List<State> terminalStates = CloneOfStateList(dfa.endStates);
//=======
////            if (!dfa.mEndStateList.contains(state)) {
////                nonTerminalStates.add(state);
////                System.out.print("State"+" = "+state+" ");
////                System.out.println("mEndStateList"+" = "+dfa.mEndStateList+ " ");
////            }
////        }
//        for (State state : copyOfOriginalState) {
//            System.out.print(state.getId() + " ");
//        }
//        System.out.println();
//        for (State state : dfa.mEndStateList) {
//            System.out.print(state.getId() + " ");
//        }
//        System.out.println();
//        for (State state : nonTerminalStates) {
//            System.out.print(state.getId() + " ");
//        }
//
//        List<State> terminalStates = CloneOfStateList(dfa.mEndStateList);
//>>>>>>> dev
//        stateListsPartition.add(nonTerminalStates);
//        stateListsPartition.add(terminalStates);
//
//        // phrase 2: 看nonTerminalStates能否再分,如果可以，则进行划分
//        int index = stateListsPartition.size() - 1;
//        //存储不在属于当前划分的状态
//        List<State> states = new ArrayList<State>();
//        List<State> statesToRemove = new ArrayList<State>();
//        states = dfa.stateList;
//
//        Set<Character> alphabetSet = new HashSet<>();
//        int count = 0;
//        for (State state : dfa.getStateList()) {                //获得所有list及set的值
//            for (Edge edge : state.getEdgeList()) {
//                alphabetSet.add(edge.getWeight());
//                count++;
//            }
//        }
//        int a = alphabetSet.size();
//        for (int i = 0; i < a; i++) {
//            for (State state : nonTerminalStates) {
//                int stateIndex = getIndexOf(states, state);
//                //System.out.println(stateIndex);
//                //获取状态state遇到下标为i的符号时状态跳转情况
////                TransitMat transitEleRow = mStateTransitionMat.get(stateIndex).get(i);
////                System.out.println(transitEleRow.getStateIndex());
////                State nextState = states.get(transitEleRow.getStateIndex());
//                TransitMat currState = dfa.mStateTransitionMat.get(stateIndex).get(i);
//                if(currState.getStateIndex() == -1){
//                    continue;
//                }
//                State nextState = states.get(currState.getStateIndex());
//                if (!nonTerminalStates.contains(nextState)) {
//                    //经过状态转移达到的状态不包含在状态集合statesToCheck中，
//                    //则nonTerminalStates继续划分为state和去掉state之后的nonTerminalStates
//                    stateListsPartition.add(stateListsPartition.size() - 1, Arrays.asList(state));
//                    statesToRemove.add(state);
//                    if (index > stateListsPartition.size() - 1) {
//                        index = stateListsPartition.size() - 1;
//                    }
//                }
//            }
//        }
//        nonTerminalStates.removeAll(statesToRemove);  //移除不再属于当前划分的状态
//
//        // phrase 3: 看terminalStates能否再分，如果可以，则进行划分
//<<<<<<< HEAD
//        int leftMostEndStateIndex = splitStateListIfCould(dfa,stateListsPartition, terminalStates);
//=======
//        index = stateListsPartition.size() - 1;
//        statesToRemove.clear();
//
//        for (int i = 0; i < a; i++) {
//            for (State state : terminalStates) {
//                int stateIndex = getIndexOf(states, state);
//                //获取状态state遇到下标为i的符号时状态跳转情况
//
////                TransitMat transitEleRow =
////                        mStateTransitionMat.get(stateIndex).get(i);
////                State nextState = states.get(transitEleRow.getStateIndex());
//                TransitMat currState = dfa.mStateTransitionMat.get(stateIndex).get(i);
//                if(currState.getStateIndex() == -1){
//                    continue;
//                }
//                State nextState = states.get(currState.getStateIndex());
//                if (!terminalStates.contains(nextState)) {
//                    //经过状态转移达到的状态不包含在状态集合statesToCheck中，
//                    //则nonTerminalStates继续划分为state和去掉state之后的nonTerminalStates
//                    stateListsPartition.add(stateListsPartition.size() - 1, Arrays.asList(state));
//                    statesToRemove.add(state);
//                    if (index > stateListsPartition.size() - 1) {
//                        index = stateListsPartition.size() - 1;
//                    }
//                }
//            }
//        }
//        terminalStates.removeAll(statesToRemove);  //移除不再属于当前划分的状态
//
//        int leftMostEndStateIndex = index;
//>>>>>>> dev
//
//        // phrase 4: 根据存储状态列表的列表的每一个元素作为一个状态，构造最小化DFA
//        rebuildDFAWithSimplifiedStateList(dfa,stateListsPartition, leftMostEndStateIndex);
//        return dfa;*/
//    }
//    private static int getIndexOf(List<State> states, State state) {
//        int i = 0;
//        for (State cmpState : states) {
//            if (cmpState.getId() == state.getId()) {
//                return i;
//            }
//            i++;
//        }
//        return -1;
//    }

    /**
     * 从DFA中获取状态转移矩阵
     */
    public static List<List<TransitMat>> GetTransitMat(DFA dfa){
        Set<Character> alphabetSet = new HashSet<>();
        List<Integer> startOfEdgeList = new ArrayList<>();
        List<Integer> endOfEdgeList = new ArrayList<>();
        List<Character> alphabetList = new ArrayList<>();
        int count = 0;                                       //边的数量
        for (State state : dfa.getStateList()) {                //获得所有list及set的值
            for (Edge edge : state.getEdgeList()) {
                alphabetSet.add(edge.getWeight());
                startOfEdgeList.add(edge.getStartState().getId());
                endOfEdgeList.add(edge.getEndState().getId());
                alphabetList.add(edge.getWeight());
                count++;
            }
        }
        List<Character> alphaSetList = new ArrayList<Character>();
        alphaSetList.addAll(alphabetSet);
        /*开始构建矩阵*/
        int a = dfa.getStateList().size();
        int b = alphabetSet.size();
        System.out.print(a+" "+b+'\n');
        Integer stateTable[][] = new Integer[a][b];
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                stateTable[i][j] = -1;
            }
        }

        int i = 0, j, flag;
        for (int temp = 0; temp < count; temp++) {
            j = 0;
            flag = 0;
            while (startOfEdgeList.get(temp) == i) {
                if (alphaSetList.get(j).equals(alphabetList.get(temp))) {
                    stateTable[i][j] = endOfEdgeList.get(temp);
                    flag = 1;
                    break;
                }
                j++;
            }
            if (temp != count - 1 && !(flag == 1 && startOfEdgeList.get(temp + 1) == i)) {
                i++;
            }
        }
        for (i = 0; i < a; i++) {
            for (j = 0; j < b; j++) {
                System.out.print(stateTable[i][j] + " ");
            }
            System.out.print('\n');
        }

        List<List<TransitMat>> stateTransitionMat = new ArrayList<List<TransitMat>>();
        for (i = 0; i < dfa.getStateList().size(); i++) {
            stateTransitionMat.add(new ArrayList<TransitMat>());
            for (j = 0; j < alphabetSet.size(); j++) {
                int stateIndex = stateTable[i][j];
                TransitMat transitEle =
                        new TransitMat(j, stateIndex);
                stateTransitionMat.get(i).add(transitEle);
            }
        }
        return stateTransitionMat;
    }

    public static List<State> CloneOfStateList(List<State> states) {
        List<State> copyOfStateList = new ArrayList<State>();
        for (State state : states) {
            // 不用FAState copyOfState = new FAState(state.getId()); 浅度复制String对象
            State copyOfState = new State(state.getId(), false);
            copyOfStateList.add(copyOfState);
        }
        return copyOfStateList;
    }

    /**
     * 判断一个状态列表能否再分，如果可以，则继续划分该列表为多个列表
     * @param stateLists 存储划分得到的状态列表的列表
     * @param statesToCheck 待划分状态列表
     */
    /*
    private static int splitStateListIfCould(DFA dfa,List<List<State>> stateLists,
                                             List<State> statesToCheck) {

        int index = stateLists.size() - 1;
        //存储不在属于当前划分的状态
        Set<Character> alphabetSet = new HashSet<>();
        for (State state : dfa.getStates()) {                //获得所有list及set的值
            for (Edge edge : state.getEdgeList()) {
                alphabetSet.add(edge.getWeight());
            }
        }
        int a = alphabetSet.size();
        List<State> statesToRemove = new ArrayList<State>();
        int stateIndex = 0;
        for (int i = 0; i < a; i++)
            for (State state : statesToCheck) {
                for (int j = 0; j < dfa.statesList.size(); j++) {
//                int stateIndex = dfa.states.indexOf(state);
                    if (state.getId() == dfa.statesList.get(j).getId()) {
                        stateIndex = j;
                    }
                }
                //获取状态state遇到下标为i的符号时状态跳转情况
                TransitMat transitEleRow =
                        dfa.stateTransitionMat.get(stateIndex).get(i);
                if (transitEleRow.getStateIndex() == -1) {
                    continue;
                }
                State nextState = dfa.statesList.get(transitEleRow.getStateIndex());
                boolean contain = false;
                for (int k = 0; k < statesToCheck.size(); k++) {
//                            if(!statesToCheck.contains(nextState)) {
                    if (statesToCheck.get(k).getId() == nextState.getId()) {
                        contain = true;
                    }
                }
                if(contain==false){
                    //经过状态转移达到的状态不包含在状态集合statesToCheck中，
                    //则nonTerminalStates继续划分为state和去掉state之后的nonTerminalStates
                    stateLists.add(stateLists.size() - 1, Arrays.asList(state));
                    statesToRemove.add(state);
                    if (index > stateLists.size() - 1) {
                        index = stateLists.size() - 1;
                    }
                }
            }

        statesToCheck.removeAll(statesToRemove);  //移除不再属于当前划分的状态
        return index;
    }
*/
    /**
     * 根据存储状态列表的列表的每一个元素作为一个状态，构造最小化DFA
     * @param stateLists 存储状态列表的列表
     */
    /*
    private static void rebuildDFAWithSimplifiedStateList(DFA dfa,List<List<State>> stateLists,int leftMostEndStateIndex) {
        List<State> copyOfStates = CloneOfStateList(dfa.stateList);
//        System.out.println(copyOfStates);
        dfa.stateList.clear();
<<<<<<< HEAD
        List<List<TransitMat>> copyOfTransitMat = deepCloneOfStateTransitionMat(dfa.stateTransitionMat);
        dfa.stateTransitionMat.clear();
=======
        List<List<TransitMat>> copyOfTransitMat = deepCloneOfStateTransitionMat(dfa.mStateTransitionMat);
        System.out.println(copyOfTransitMat.size());
        dfa.mStateTransitionMat.clear();
>>>>>>> dev

        // phrase 1: 重新构造状态列表
        rebuildStateList(dfa, stateLists, leftMostEndStateIndex);

        // phrase 2: 重新构建状态转移矩阵
        rebuildStateTransitMat(dfa, copyOfStates, copyOfTransitMat, stateLists);
    }

    private static List<List<TransitMat>> deepCloneOfStateTransitionMat(List<List<TransitMat>> stateTransitionMat) {
        List<List<TransitMat>> copyOfStateTransitionMat =
                new ArrayList<List<TransitMat>>();
        for(List<TransitMat> rowOfStateTransitMat: stateTransitionMat) {
            List<TransitMat> copyOfStateTransitList =
                    new ArrayList<TransitMat>();
            for(TransitMat transitEle: rowOfStateTransitMat) {
                TransitMat copyOfTransitEle =
                        new TransitMat(transitEle.getAlphaIndex(),transitEle.getStateIndex());
                copyOfStateTransitList.add(copyOfTransitEle);
            }
            copyOfStateTransitionMat.add(copyOfStateTransitList);
        }
        return copyOfStateTransitionMat;
    }

<<<<<<< HEAD
=======

//    /**
//     * 判断一个状态列表能否再分，如果可以，则继续划分该列表为多个列表
//     * @param stateLists 存储划分得到的状态列表的列表
//     * @param statesToCheck 待划分状态列表
//     */
//    private int splitStateListIfCould(List<List<State>> stateLists,
//                                      List<State> statesToCheck) {
//        int index = stateLists.size() - 1;
//        //存储不在属于当前划分的状态
//        List<State> statesToRemove = new ArrayList<State>();
//        for(int i=0; i<this.alphabet.size(); i++) {
//            for(State state : statesToCheck) {
//                int stateIndex = this.states.indexOf(state);
//                //获取状态state遇到下标为i的符号时状态跳转情况
//                TransitMat transitEleRow =
//                        this.mStateTransitionMat.get(stateIndex).get(i);
//                State nextState = this.states.get(transitEleRow.getStateIndex());
//                if(!statesToCheck.contains(nextState)) {
//                    //经过状态转移达到的状态不包含在状态集合statesToCheck中，
//                    //则nonTerminalStates继续划分为state和去掉state之后的nonTerminalStates
//                    stateLists.add(stateLists.size()-1, Arrays.asList(state));
//                    statesToRemove.add(state);
//                    if(index > stateLists.size() - 1) {
//                        index = stateLists.size() - 1;
//                    }
//                }
//            }
//        }
//        statesToCheck.removeAll(statesToRemove);  //移除不再属于当前划分的状态
//        return index;
//    }

//>>>>>>> dev
    /**
     * 重新构造状态列表
     * @param stateLists 包含状态列表的列表
     * @param leftMostEndStateIndex 可接受状态中下标最小的状态在stateLists中的下标
     */
    /*
    private static void rebuildStateList (DFA dfa,List<List<State>> stateLists,
                                          int leftMostEndStateIndex){
        Random random = new Random();
        //stateLists中的第一个元素中的所有状态构成新的DFA对象的开始状态
        dfa.mStartState =
                new State(random.nextInt(),false);
        dfa.stateList.add(dfa.mStartState);

        //添加既不是开始状态节点，也不是可接受状态节点的状态节点
        for (int i = 1; i < leftMostEndStateIndex; i++) {
            State newState =
                    new State(random.nextInt(),false);
            dfa.stateList.add(newState);
        }

        // stateLists中原来DFA对象的可接受状态构成新的DFA对象的可接受状态
        for (int i = leftMostEndStateIndex; i < stateLists.size(); i++) {
            State newState =
                    new State(random.nextInt(),true);
            dfa.mEndStateList.add(newState);
            dfa.stateList.add(newState);
        }
    }*/

    /**
     * 为最小化DFA重新构造状态转移矩阵
     * @param originalStateList 原来DFA的状态列表
     * @param originalStateTransitMat 原来DFA的状态转移矩阵
     * @param stateLists 存储状态列表的列表
     */
    private static void rebuildStateTransitMat(DFA dfa,List<State> originalStateList,
                                               List<List<TransitMat>> originalStateTransitMat,
                                               List<List<State>> stateLists) {
        for(int i=1; i<stateLists.size(); i++) {
            List<State> stateList = stateLists.get(i);		//当前状态划分
            List<TransitMat> stateTransitEleRow =
                    new ArrayList<TransitMat>();
            //建立到其它划分中状态的状态转移
            buildTransitWithStatesInOtherPartition(
                    originalStateList,originalStateTransitMat,
                    stateLists, stateList, stateTransitEleRow);
            //建立划分内的状态转移(针对划分内某个状态遇到某一符号串不转向下一状态的情况)
            buildTransitWithStatesInInnerPartition(
                    originalStateList, originalStateTransitMat,
                    stateLists, stateList, stateTransitEleRow);
            dfa.mStateTransitionMat.add(stateTransitEleRow);
        }
    }

    //建立划分内的状态转移(针对划分内某个状态遇到某一符号串不转向下一状态的情况)
    private static void buildTransitWithStatesInInnerPartition(
            List<State> originalStateList,
            List<List<TransitMat>> originalStateTransitMat,
            List<List<State>> stateLists, List<State> stateList,
            List<TransitMat> stateTransitEleRow) {
        for(State stateInPartition : stateList) {
//            int stateIndex = originalStateList.indexOf(stateInPartition);
            int stateIndex = 0;
            for (int i = 0; i < originalStateList.size(); i++) {
                if (stateInPartition.getId() == originalStateList.get(i).getId()) {
                    stateIndex = i;
                }
            }
            List<TransitMat> transitEleRow =
                    originalStateTransitMat.get(stateIndex);
            for(TransitMat transitEle : transitEleRow) {
                if(transitEle.getStateIndex() == stateIndex) { //在该状态上存在转向自己的循环
                    //获取在最小化的DFA对象中的下标
                    int currentStateIndex =
                            getStateIndexInNewDFA(stateLists, stateInPartition);
                    stateTransitEleRow.add(
                            new TransitMat(transitEle.getAlphaIndex(), currentStateIndex));
                }
            }
        }
    }

    //建立到其它划分中状态的状态转移
    private static void buildTransitWithStatesInOtherPartition(
            List<State> originalStateList,
            List<List<TransitMat>> originalStateTransitMat,
            List<List<State>> stateLists, List<State> stateList,
            List<TransitMat> stateTransitEleRow) {
//        System.out.println("stateList.get(0):"+stateList.get(0).getId());
        //int originalStateIndex = originalStateList.indexOf(stateList.get(0));
        int originalStateIndex = 0;
        for(int i=0;i<originalStateList.size();i++){
            if(originalStateList.get(i).getId()==stateList.get(0).getId())
                originalStateIndex = i;
        }

        List<TransitMat> stateTransitMatRow =
                originalStateTransitMat.get(originalStateIndex);
        for (TransitMat transitEle : stateTransitMatRow) {
            //当前转向的状态
            if(transitEle.getStateIndex()==-1){
                continue;
            }
            State currentState = originalStateList.get(transitEle.getStateIndex());
            if (!stateList.contains(currentState)) {
                //不是在同一个划分中，存在到其它划分则状态的状态转移
                int currentStateIndex = getStateIndexInNewDFA(stateLists, currentState);
                stateTransitEleRow.add(
                        new TransitMat(transitEle.getAlphaIndex(), currentStateIndex));
            }
        }
    }

    /**
     * 查找某一状态在最小化DFA中所在划分的下标
     * @param stateLists 状态列表的划分
     * @param state 将要查找的FAState对象
     * @return 某一状态在最小化DFA中所在划分的下标
     */
    private static int getStateIndexInNewDFA(List<List<State>> stateLists, State state) {
        for(int i=0; i<stateLists.size(); i++) {
            List<State> currentStateList = stateLists.get(i);
            if(currentStateList.contains(state)) {
                return i;
            }
        }
        return -1;
    }
}
