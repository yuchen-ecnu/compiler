import com.ecnu.compiler.component.lexer.domain.DFA;
import com.ecnu.compiler.component.lexer.domain.NFA;
import com.ecnu.compiler.component.lexer.domain.RE;
import com.ecnu.compiler.component.lexer.domain.graph.Edge;
import com.ecnu.compiler.component.lexer.domain.graph.State;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void RE2NFA() throws Exception {
        RE expression = new RE("test", "(a|b)*d");
        NFA nfa = expression.getNFA();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nfa.getStartState().getId()).append(" <-> ")
                .append(nfa.getEndState().getId()).append("  in ").append(nfa.getStateList().size()).append('\n');
        int count = 0;
        for (State state : nfa.getStateList()){
            for (Edge edge : state.getEdgeList()){
                stringBuilder.append(edge.getStartState().getId()).append(" -").append(edge.getWeight()).append("> ")
                        .append(edge.getEndState().getId()).append('\n');
                count++;
            }
        }
        stringBuilder.append("total edges: ").append(count);
        System.out.println(stringBuilder.toString());
    }

    @Test
    public void RE2DFA() throws Exception {
        RE expression = new RE("test", "(a|b)*abb#");
        DFA dfa = expression.getDFADirectly();
    }

    @Test
    public void NFA2DFA() throws Exception {
        RE expression = new RE("test", "(a|b)*d");
        DFA dfa = expression.getDFAIndirect();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (State state : dfa.getStateList()){
            for (Edge edge : state.getEdgeList()){
                stringBuilder.append(edge.getStartState().getId()).append(" -").append(edge.getWeight()).append("> ")
                        .append(edge.getEndState().getId()).append('\n');
                count++;
            }
        }
        stringBuilder.append("total edges: ").append(count);
        System.out.println(stringBuilder.toString());
        System.out.println("startState: "+ dfa.getStartState().getId());
        for(State state :dfa.getEndStateList()){
            System.out.println("endStates: " + state.getId());
        }
    }

    @Test
    public void DFA2MinDFA() throws Exception {
    }

    @Test
    public void DFAMatch() throws Exception {
        RE expression = new RE("test", "(a|b)*a");
        DFA dfa = expression.getDFAIndirect();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (State state : dfa.getStateList()){
            for (Edge edge : state.getEdgeList()){
                stringBuilder.append(edge.getStartState().getId()).append(" -").append(edge.getWeight()).append("> ")
                        .append(edge.getEndState().getId()).append('\n');
                count++;
            }
        }
        stringBuilder.append("total edges: ").append(count);
        System.out.println(stringBuilder.toString());
        System.out.println("startState: "+ dfa.getStartState().getId());
        for(State state :dfa.getEndStateList()){
            System.out.println("endStates: " + state.getId());
        }
        String lexemes = "abababaaba";
        if(dfa.match(lexemes)!=null && !dfa.match(lexemes).isEmpty()){
            System.out.println("Accepted!");
        }else{
            System.out.println("Error!");
        }
    }
}