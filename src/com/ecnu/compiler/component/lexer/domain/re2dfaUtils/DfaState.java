package com.ecnu.compiler.component.lexer.domain.re2dfaUtils;

import com.ecnu.compiler.component.lexer.domain.graph.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DfaState extends State {
    
    private Set<Integer> name;
    //move(str, s)表示可以从当前state通过str迁移到s状态
    private HashMap<String, DfaState> move;
    
    private boolean isMarked;

    public DfaState(int id){
        super(id, false);
        this.id = id;
        move = new HashMap<>();
        name = new HashSet<>();
        isAccepted = false;
        isMarked = false;
    }
    
    public void addMove(String symbol, DfaState s){
        move.put(symbol, s);
    }
    
    public void addToName(int number){
        name.add(number);
    }

    public void addAllToName(Set<Integer> number){
        name.addAll(number);
    }
    
    public void setMarked(boolean bool){
        isMarked = bool;
    }
    
    public boolean getMarked(){
        return isMarked;
    }
    
    public Set<Integer> getName(){
        return name;
    }

    public void setAccepted() {
        isAccepted = true;
    }
    
    public boolean getAccepted(){
        return isAccepted;
    }
    
    public DfaState getNextStateBySymbol(String str){
        return this.move.get(str);
    }
    
    public HashMap<String, DfaState> getAllMoves() {
        return move;
    }

    public String showMoves() {
        Iterator it = move.entrySet().iterator();
        String moveString = "";
        while(it.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) it.next();
            String key = "(" + entry.getKey() + ",";
            String val = ((DfaState) entry.getValue()).getId() + "), ";
            moveString = moveString + key + val;
        }
        return moveString;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name=" + name +
                ", move=" + showMoves() +
                "isMarked=" + isMarked +
                ", isAccepted=" + isAccepted +
                "}\n";
    }
}
