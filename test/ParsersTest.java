import com.ecnu.compiler.component.parser.domain.CFG;
import com.ecnu.compiler.component.parser.domain.FirstFollowSet;
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
        String[] cfgList = {"E->T E'", "E'->+ T E' |epsilon", "T->F T'", "T'->* F T'|epsilon", "F->( E )|id"};
        Symbol s1 = new Symbol("E");
        Symbol s2 = new Symbol("T");
        Symbol s3 = new Symbol("E'");
        Symbol s4 = new Symbol("+");
        Symbol s5 = new Symbol("epsilon");
        Symbol s6 = new Symbol("T'");
        Symbol s7 = new Symbol("*");
        Symbol s8 = new Symbol("F");
        Symbol s9 = new Symbol("(");
        Symbol s10 = new Symbol(")");
        Symbol s11 = new Symbol("id");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        symbolSet.add(s4);
        symbolSet.add(s5);
        symbolSet.add(s6);
        symbolSet.add(s7);
        symbolSet.add(s8);
        symbolSet.add(s9);
        symbolSet.add(s10);
        symbolSet.add(s11);
        CFG cfg = new CFG(cfgList, symbolSet);
        System.out.println("Start Symbol:" + cfg.getStartSymbol().getType());
        System.out.println("------------");
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
        System.out.println("-------------");
        System.out.println("First:");
        FirstFollowSet firstFollowSet = new FirstFollowSet(cfg);
        Map<Symbol, Set<Symbol>> firstMap = firstFollowSet.getFirstMap();
        Map<Symbol, Set<Symbol>> followMap = firstFollowSet.getFollowMap();
        for (Map.Entry<Symbol, Set<Symbol>> entry : firstMap.entrySet()) {
            System.out.print(entry.getKey().getType() + "----");
            if (entry.getValue() != null) {
                for (Symbol s : entry.getValue()) {
                    System.out.print(s.getType() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("------------");
        System.out.println("Follow:");
        for (Map.Entry<Symbol, Set<Symbol>> entry : followMap.entrySet()) {
            System.out.print(entry.getKey().getType() + "----");
            if (entry.getValue() != null) {
                for (Symbol s : entry.getValue()) {
                    System.out.print(s.getType() + " ");
                }
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
