import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.Production;
import com.ecnu.compiler.component.parser.domain.Symbol;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParsersTest {

    @Test
    public void LLParser() throws Exception {
        String[] cfgList = {"E->E+T|T", "T->T*F|F", "F->(E)|id"};
        Symbol s1 = new Symbol("E");
        Symbol s2 = new Symbol("+");
        Symbol s3 = new Symbol("T");
        Symbol s4 = new Symbol("*");
        Symbol s5 = new Symbol("F");
        Symbol s6 = new Symbol("(");
        Symbol s7 = new Symbol(")");
        Symbol s8 = new Symbol("id");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        symbolSet.add(s4);
        symbolSet.add(s5);
        symbolSet.add(s6);
        symbolSet.add(s7);
        symbolSet.add(s8);
        CFG cfg = new CFG(cfgList, symbolSet);
        for(Map.Entry<Symbol, List<Integer>> entry : cfg.getNonTerminalMap().entrySet()) {
            System.out.print(entry.getKey().getType() + "----");
            for (Integer i : entry.getValue()) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println("------------");
        for(Symbol s : cfg.getTerminalSet()) {
            System.out.println(s.getType() + "----" + s.isTerminal());
        }
        System.out.println("------------");
        for (Production p : cfg.getAllProductions()) {
            System.out.println("Id:" + p.getId());
            System.out.println("Left:" + p.getLeft().getType());
            System.out.print("Right:");
            for (Symbol s : p.getRight()) {
                System.out.print(s.getType() + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void LRParser() throws Exception {

    }

    @Test
    public void SLRParser() throws Exception {

    }

    @Test
    public void LALRParser() throws Exception {

    }

}
