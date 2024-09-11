package SyntaxAnalyzer;

import java.util.HashMap;
import java.util.HashSet;

public class Firsts {

    static HashMap<String, HashSet<String>> firsts = createMap();

    static HashMap<String, HashSet<String>> createMap() {
        HashMap<String, HashSet<String>> firsts = new HashMap<>();
        HashSet<String> classFirsts = new HashSet<>();
        classFirsts.add("rw_class");
        firsts.put("ClassList", classFirsts);
        firsts.put("Class", classFirsts);
        return firsts;
    }

    static boolean isFirst(String nonTerminal, String lexeme) {
        return firsts.get(nonTerminal).contains(lexeme);
    }

}
