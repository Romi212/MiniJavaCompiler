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

        HashSet<String> classFollow = new HashSet<>();
        classFollow.add("rw_class");
        classFollow.add("EOF");
        follows.put("Class", classFollow);

        HashSet<String> memberFollow = new HashSet<>();
        memberFollow.add("rw_static");
        memberFollow.add("rw_void");
        memberFollow.add("id_class");
        memberFollow.add("rw_int");
        memberFollow.add("rw_boolean");
        memberFollow.add("rw_char");
        memberFollow.add("EOF");
        follows.put("Member", memberFollow);

        HashSet<String> declarationFollow = new HashSet<>();
        declarationFollow.add("rw_static");
        declarationFollow.add("rw_void");
        declarationFollow.add("id_class");
        declarationFollow.add("rw_int");
        declarationFollow.add("rw_boolean");
        declarationFollow.add("rw_char");
        declarationFollow.add("EOF");
        follows.put("Declaration", declarationFollow);

        HashSet<String> staticFollow = new HashSet<>();
        staticFollow.add("rw_void");
        staticFollow.add("id_class");
        staticFollow.add("rw_int");
        staticFollow.add("rw_boolean");
        staticFollow.add("rw_char");
        staticFollow.add("EOF");
        follows.put("Static", staticFollow);

        HashSet<String> memberTypeFollow = new HashSet<>();
        memberTypeFollow.add("id_class");
        memberTypeFollow.add("EOF");
        follows.put("MemberType", memberTypeFollow);

        HashSet<String> typeFollow = new HashSet<>();
        typeFollow.add("id_class");
        typeFollow.add("EOF");
        follows.put("Type", typeFollow);

        HashSet<String> primitiveTypeFollow = new HashSet<>();
        primitiveTypeFollow.add("id_class");
        primitiveTypeFollow.add("EOF");
        follows.put("PrimitiveType", primitiveTypeFollow);

        HashSet<String> constructorFollow = new HashSet<>();
        constructorFollow.add("EOF");
        follows.put("Constructor", constructorFollow);

        HashSet<String> bodyFollow = new HashSet<>();
        bodyFollow.add("EOF");
        follows.put("Body", bodyFollow);

        HashSet<String> formalArgFollow = new HashSet<>();
        formalArgFollow.add("pm_comma");

        return follows;
    }
}