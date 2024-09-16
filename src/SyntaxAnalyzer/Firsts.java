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
        firsts.put("FormalArg", new HashSet<String>() {{ add("rw_char"); add("rw_int"); add("rw_boolean"); add("id_class"); }});

        firsts.put("LocalVar", new HashSet<String>() {{ add("rw_var"); }});
        firsts.put("Return", new HashSet<String>() {{ add("rw_return"); }});
        firsts.put("Break", new HashSet<String>() {{ add("rw_break"); }});
        firsts.put("If", new HashSet<String>() {{ add("rw_if"); }});
        firsts.put("While", new HashSet<String>() {{ add("rw_while"); }});
        firsts.put("Switch", new HashSet<String>() {{ add("rw_switch"); }});
        HashSet<String> expFirst = new HashSet<>();
        HashSet<String> unaryOP = new HashSet<>();
        //UNaryOP
        unaryOP.add("op_add");
        unaryOP.add("op_sub");
        unaryOP.add("op_not");

        firsts.put("UnaryOp", unaryOP);
        expFirst.addAll(unaryOP);
        //Operand:
        //  Literal
        HashSet<String> litFirst = new HashSet<>();
        //      Primitive
        HashSet<String> primFirst = new HashSet<>();
        primFirst.add("rw_true");
        primFirst.add("rw_false");
        primFirst.add("lit_int");
        primFirst.add("lit_char");
        litFirst.addAll(primFirst);
        firsts.put("PrimitiveLiteral", primFirst);
        //      Object
        litFirst.add("id_null");
        litFirst.add("lit_string");
        firsts.put("Literal", litFirst);
        expFirst.addAll(litFirst);
        //  Access = Primary
        //      THis
        expFirst.add("rw_this");
        //      Constructor
        expFirst.add("rw_new");
        //      SMethod
        expFirst.add("id_class");
        //      PExpression
        expFirst.add("pm_par_open");
        //      VarMet
        expFirst.add("id_met_var");

        firsts.put("Expression", expFirst);
        firsts.put("ComplexExpression", expFirst);
        firsts.put("BasicExpression", expFirst);
        firsts.put("Statement", new HashSet<String>() {{ addAll(expFirst); add("rw_var"); add("rw_return"); add("rw_break"); add("rw_if"); add("rw_while"); add("rw_switch"); add("pm_semicolon"); add("pm_brace_open"); }});

        firsts.put("SwitchStatement", new HashSet<String>() {{ add("rw_case"); add("rw_default"); }});

        firsts.put("AssignmentOp", new HashSet<String>() {{ add("assign"); add("assign_add"); add("assign_sub"); }});

        firsts.put("AccessThis", new HashSet<String>() {{ add("rw_this"); }});
        firsts.put("AccessConstructor", new HashSet<String>() {{ add("rw_new"); }});
        firsts.put("AccessStaticMethod", new HashSet<String>() {{ add("id_class"); }});
        firsts.put("AccessPExpression", new HashSet<String>() {{ add("pm_par_open"); }});

        firsts.put("ActualArgs", new HashSet<String>() {{ add("pm_par_open"); }});

        firsts.put("BinaryOp", new HashSet<String>() {{ add("op_and"); add("op_or"); add("op_equal"); add("op_not_equal");  add("op_greater"); add("op_less"); add("op_greater_equal"); add("op_less_equal"); add("op_add"); add("op_sub"); add("op_mul"); add("op_div"); add("op_mod"); }});

        return firsts;
    }

    static boolean isFirst(String nonTerminal, String lexeme) {
        return firsts.get(nonTerminal).contains(lexeme);
    }

}
