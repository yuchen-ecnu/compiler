package com.ecnu.compiler.component.lexer.domain;

import com.ecnu.compiler.component.lexer.domain.base.Graph;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import com.ecnu.compiler.component.lexer.domain.re2dfaUtils.DfaState;

import java.io.IOException;
import java.util.*;

/**
 * 确定性有限状态自动机
 * @author Michael Chen
 */
public class DFA extends Graph {

    private List<DfaState> stateList;

    private List<State> states;

    private List<State> endStates;

    private State startDfaState;

    private State curState;

    private DfaState startState;

    private List<DfaState> endStateList;

    public List<List<TransitMat>> stateTransitionMat;

    public DFA() { }

    public DFA(List<DfaState> dfaStateList,List<List<TransitMat>> stateTransitionMat) {
        this.setStateList(dfaStateList);
        this.stateTransitionMat = stateTransitionMat;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getStartDfaState() {
        return startDfaState;
    }

    public void setStartDfaState(State startDfaState) {
        this.startDfaState = startDfaState;
    }

    public List<DfaState> getDfaStateList() {
        return stateList;
    }

    public void setStateList(List<DfaState> dfaStateList) {
        this.stateList = dfaStateList;
    }

    public void setStartState(DfaState startState) {
        this.startState = startState;
    }

    public void setEndStateList(List<DfaState> endStateList) {
        this.endStateList = endStateList;
    }

    public State take(char input){
        return null;
    }

    public boolean isEndState(){
        return curState != null && curState.isAccepted;
    }

    public void print() {
        String states = "States:\n";
        String edges = "Edges:\n";
        String endStates = "End States:\n";
        for(DfaState s : stateList) {
            states += s.toString();
            for(Edge e : s.getEdgeList()){
                edges += e.toStringForDfa();
            }
        }
        for(DfaState s : endStateList) {
            endStates = endStates + s.getName() + "\n";
        }
        System.out.println(states);
        System.out.println(edges);
        System.out.println("Start State:\n" + startState.getName());
        System.out.println(endStates);
    }

    public int getMaxId(){
        int maxId=0;
        for(State s : this.states){
            if(s.getId()>maxId){
                maxId=s.getId();
            }
        }
        return maxId;
    }

    public State getStateById(int id) {
        for (State state : states) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public void setEndStateList(){
        this.endStates = new ArrayList<>();
        for(State state:states){
            if(state.getEdgeList().isEmpty()){
                state.isAccepted = true;
                endStates.add(state);
            }
        }
    }

    public List<State> getEndStates() {
        return endStates;
    }

    public void setEndStates(List<State> endStates) {
        this.endStates = endStates;
    }

    /**
     * minimize DFA
     *
     * @param dfa DFA
     * @return DFA
     * @author Chen Jianing
     **/

    public static DFA DFA2MinDFA(DFA dfa) throws IOException {
        dfa.stateTransitionMat = GetTransitMat(dfa);
        //用于存放最小化的过程中产生的状态划分
        List<List<State>> stateListsPartition = new ArrayList<List<State>>();

        //phrase 1: 将化简前的DFA的状态分为非可接受状态和可接受状态两部分
        List<State> nonTerminalStates = new ArrayList<State>();
        List<State> copyOfOriginalState = CloneOfStateList(dfa.states);
        for (State state : copyOfOriginalState) {
            for(State endState : dfa.endStates){
                if(state.getId()!=endState.getId()) {
                    nonTerminalStates.add(state);
                }
            }
        }
        List<State> terminalStates = CloneOfStateList(dfa.endStates);
        stateListsPartition.add(nonTerminalStates);
        stateListsPartition.add(terminalStates);

        // phrase 2: 看nonTerminalStates能否再分,如果可以，则进行划分
        splitStateListIfCould(dfa, stateListsPartition, nonTerminalStates);

        // phrase 3: 看terminalStates能否再分，如果可以，则进行划分
        int leftMostEndStateIndex = splitStateListIfCould(dfa,stateListsPartition, terminalStates);

        // phrase 4: 根据存储状态列表的列表的每一个元素作为一个状态，构造最小化DFA
        rebuildDFAWithSimplifiedStateList(dfa,stateListsPartition, leftMostEndStateIndex);

        return dfa;
    }

    /**
     * 从DFA中获取状态转移矩阵
     */
    public static List<List<TransitMat>> GetTransitMat(DFA dfa){
        Set<Character> alphabetSet = new HashSet<>();
        List<Integer> startOfEdgeList = new ArrayList<>();
        List<Integer> endOfEdgeList = new ArrayList<>();
        List<Character> alphabetList = new ArrayList<>();
        int count = 0;                                       //边的数量
        for (State state : dfa.getStates()) {                //获得所有list及set的值
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
        int a = dfa.getStates().size();
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
                if (alphaSetList.get(j) == alphabetList.get(temp)) {
                    stateTable[i][j] = endOfEdgeList.get(temp);
                    flag = 1;
                    break;
                }
                j++;
            }
            if (temp != count - 1 && !(flag == 1 && startOfEdgeList.get(temp + 1) == i))
                i++;
        }
        for (i = 0; i < a; i++) {
            for (j = 0; j < b; j++) {
                System.out.print(stateTable[i][j] + " ");
            }
            System.out.print('\n');
        }

        List<List<TransitMat>> stateTransitionMat = new ArrayList<List<TransitMat>>();
        for (i = 0; i < dfa.getStates().size(); i++) {
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
            State copyOfState = new State(new Integer(state.getId()), false);
            copyOfStateList.add(copyOfState);
        }
        return copyOfStateList;
    }

    /**
     * 判断一个状态列表能否再分，如果可以，则继续划分该列表为多个列表
     * @param stateLists 存储划分得到的状态列表的列表
     * @param statesToCheck 待划分状态列表
     */
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
                for (int j = 0; j < dfa.states.size(); j++) {
//                int stateIndex = dfa.states.indexOf(state);
                    if (state.getId() == dfa.states.get(j).getId()) {
                        stateIndex = j;
                    }
                }
                //获取状态state遇到下标为i的符号时状态跳转情况
                TransitMat transitEleRow =
                        dfa.stateTransitionMat.get(stateIndex).get(i);
                if (transitEleRow.getStateIndex() == -1) {
                    continue;
                }
                State nextState = dfa.states.get(transitEleRow.getStateIndex());
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

    /**
     * 根据存储状态列表的列表的每一个元素作为一个状态，构造最小化DFA
     * @param stateLists 存储状态列表的列表
     */
    private static void rebuildDFAWithSimplifiedStateList(DFA dfa,List<List<State>> stateLists,int leftMostEndStateIndex) {
        List<State> copyOfStates = CloneOfStateList(dfa.states);
        dfa.states.clear();
        List<List<TransitMat>> copyOfTransitMat = deepCloneOfStateTransitionMat(dfa.stateTransitionMat);
        dfa.stateTransitionMat.clear();

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

    /**
     * 重新构造状态列表
     * @param stateLists 包含状态列表的列表
     * @param leftMostEndStateIndex 可接受状态中下标最小的状态在stateLists中的下标
     */
    private static void rebuildStateList (DFA dfa,List<List<State>> stateLists,
                                          int leftMostEndStateIndex){
        Random random = new Random();
        //stateLists中的第一个元素中的所有状态构成新的DFA对象的开始状态
        dfa.startDfaState =
                new State(random.nextInt(),false);
        dfa.states.add(dfa.startDfaState);

        //添加既不是开始状态节点，也不是可接受状态节点的状态节点
        for (int i = 1; i < leftMostEndStateIndex; i++) {
            State newState =
                    new State(random.nextInt(),false);
            dfa.states.add(newState);
        }

        // stateLists中原来DFA对象的可接受状态构成新的DFA对象的可接受状态
        for (int i = leftMostEndStateIndex; i < stateLists.size(); i++) {
            State newState =
                    new State(random.nextInt(),true);
            dfa.endStates.add(newState);
            dfa.states.add(newState);
        }
    }

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
            dfa.stateTransitionMat.add(stateTransitEleRow);
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
