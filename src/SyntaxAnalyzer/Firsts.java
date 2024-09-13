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

        HashSet<String> memberFirst = new HashSet<>();
        memberFirst.add("rw_static");
        memberFirst.add("rw_void");
        memberFirst.add("id_class");
        memberFirst.add("rw_int");
        memberFirst.add("rw_boolean");
        memberFirst.add("rw_char");
        firsts.put("Member", memberFirst);
        firsts.put("Declaration", memberFirst);
        firsts.put("Static", new HashSet<String>() {{ add("rw_static"); }});
        firsts.put("MemberType", new HashSet<String>() {{ add("rw_char"); add("rw_int"); add("rw_boolean"); add("rw_void"); add("id_class"); }});

        firsts.put("Type", new HashSet<String>() {{ add("rw_char"); add("rw_int"); add("rw_boolean"); add("id_class"); }});

        firsts.put("PrimitiveType", new HashSet<String>() {{ add("rw_char"); add("rw_int"); add("rw_boolean"); add("rw_void");  }});

        firsts.put("Constructor", new HashSet<String>() {{ add("rw_public"); }});

        firsts.put("Body", new HashSet<String>() {{ add("pm_semicolon"); add("pm_par_open"); }});
        return firsts;
    }

    static boolean isFirst(String nonTerminal, String lexeme) {
        return firsts.get(nonTerminal).contains(lexeme);
    }

}
