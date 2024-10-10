package SyntaxAnalyzer;

import java.util.HashMap;
import java.util.HashSet;

public class Follows {

    static HashMap<String, HashSet<String>> follows = createMap();

    static HashMap<String, HashSet<String>> createMap() {
        HashMap<String, HashSet<String>> follows = new HashMap<>();

        HashSet<String> classFollows = new HashSet<>();
        classFollows.add("EOF");
        follows.put("ClassList", classFollows);
        HashSet<String> formalArgFollow = new HashSet<>();
        formalArgFollow.add("pm_comma");


        follows.put("Generic", new HashSet<>(){{add("rw_extends"); add("pm_par_open"); add("pm_brace_open"); add("id_met_var");}});

        follows.put("PTypesList", new HashSet<>(){{ add("op_greater");}});
        follows.put("Parents", new HashSet<>(){{add("pm_brace_open");}});

        follows.put("MemberList", new HashSet<>(){{add("pm_brace_close");}});
        follows.put("Visibility", new HashSet<>(){{add("rw_class");}});

        follows.put("ChainedVar", new HashSet<>(){{add("assign"); add("pm_semicolon");}});

        follows.put("PossibleExp", new HashSet<>(){{add("pm_semicolon"); add("pm_par_close"); add("pm_comma"); }});

        HashSet<String> chainedOP = new HashSet<>();
        chainedOP.addAll(Firsts.firsts.get("BinaryOp"));
        chainedOP.addAll(Firsts.firsts.get("AssignmentOp"));
        chainedOP.add("pm_semicolon");
        chainedOP.add("pm_par_close");
        chainedOP.add("pm_comma");
        chainedOP.add("pm_period");

        follows.put("ChainedOp", chainedOP);
        return follows;
    }

    static boolean itFollows(String nonTerminal, String terminal){
        return follows.get(nonTerminal).contains(terminal);
    }
}
