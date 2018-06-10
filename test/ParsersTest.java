import com.ecnu.compiler.component.parser.LLParser;
import com.ecnu.compiler.component.parser.domain.*;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLParsingTable;
import com.ecnu.compiler.component.parser.domain.ParsingTable.LLTableItem;
import com.ecnu.compiler.component.parser.domain.PredictTable.PredictTable;
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
        System.out.println("------------");
        LLParsingTable llParsingTable = new LLParsingTable(cfg);
        Set<LLTableItem> itemSet = llParsingTable.getItemSet();
        for (LLTableItem item : itemSet) {
            System.out.println("Non-term:" + item.getNonTerm().getType());
            System.out.println("Term:" + item.getTerm().getType());
            System.out.print("Value:" + item.getValue().getLeft().getType() + " -> ");
            for (Symbol s : item.getValue().getRight()) {
                System.out.print(s.getType() + " ");
            }
            System.out.println();
            System.out.println("-----------");
        }
        System.out.println("------------");
        String w = "id + id * id";
        TD syntaxTree = LLParser.predict(w, cfg);
        PredictTable predictTable = LLParser.getPredictTable();
        System.out.println("----------------------");
        System.out.println(predictTable);
        System.out.println("----------------------");
        LLParser.printTree(syntaxTree);
        System.out.println("----------------------");

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

    @Test
    public void cleanLeftRecursion() throws Exception {
        String[] cfgList = {"T->T * F", "T->F"};
        Symbol s1 = new Symbol("T");
        Symbol s2 = new Symbol("*");
        Symbol s3 = new Symbol("F");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        CFG cfg = new CFG(cfgList, symbolSet);
        cfg.cleanImmediateLeftRecursion(s1);
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
    public void cleanLeftRecusrion() throws Exception {
        String[] cfgList = {"E->E + T|T", "T->T * F|F", "F->( E )|id"};
        Symbol s1 = new Symbol("E");
        Symbol s2 = new Symbol("+");
        Symbol s3 = new Symbol("T");
        Symbol s4 = new Symbol("*");
        Symbol s5 = new Symbol("F");
        Symbol s6 = new Symbol("id");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        symbolSet.add(s4);
        symbolSet.add(s5);
        symbolSet.add(s6);
        CFG cfg = new CFG(cfgList, symbolSet);
        cfg.cleanLeftRecursion();
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
    public void extractLeftCommonFactor() throws Exception {
        String[] cfgList = {"A->+ a b|+ b B|b", "B->+ a b|+ a|+"};
        Symbol s1 = new Symbol("A");
        Symbol s2 = new Symbol("+");
        Symbol s3 = new Symbol("a");
        Symbol s4 = new Symbol("b");
        Symbol s5 = new Symbol("B");
        Set<Symbol> symbolSet = new HashSet<>();
        symbolSet.add(s1);
        symbolSet.add(s2);
        symbolSet.add(s3);
        symbolSet.add(s4);
        symbolSet.add(s5);
        CFG cfg = new CFG(cfgList, symbolSet);
        cfg.extractLeftCommonFactor();
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
}
