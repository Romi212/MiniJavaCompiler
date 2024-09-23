package SyntaxAnalyzer;

import java.util.HashMap;
import java.util.HashSet;

public class Firsts {

    static HashMap<String, HashSet<String>> firsts = createMap();

    static HashMap<String, HashSet<String>> createMap() {
        HashMap<String, HashSet<String>> firsts = new HashMap<>();

        HashSet<String> classFirsts = new HashSet<>();
        classFirsts.add("rw_class");
        classFirsts.add("rw_abstract");
        firsts.put("ClassList", classFirsts);
        firsts.put("Class", classFirsts);

        HashSet<String> primitiveType = new HashSet<>();
        primitiveType.add("rw_char");
        primitiveType.add("rw_int");
        primitiveType.add("rw_boolean");

        HashSet<String> memberFirst = new HashSet<>();
        memberFirst.add("rw_static");
        memberFirst.add("rw_void");
        memberFirst.add("id_class");
        memberFirst.addAll(primitiveType);
        HashSet<String> memberTrue = new HashSet<>();
        memberTrue.addAll(memberFirst);
        memberTrue.add("rw_public");
        memberTrue.add("rw_private");
        firsts.put("VisibleMember", memberTrue);
        firsts.put("Member", memberFirst);
        firsts.put("Static", new HashSet<String>() {{ add("rw_static"); }});
        firsts.put("MemberType", new HashSet<String>() {{ addAll(primitiveType); add("rw_void"); add("id_class"); }});
        firsts.put("Type", new HashSet<String>() {{ addAll(primitiveType); add("id_class"); }});
        firsts.put("PrimitiveType",primitiveType);
        firsts.put("NonObjectType", new HashSet<String>() {{ addAll(primitiveType);add("rw_void");   }});

        firsts.put("LocalType", new HashSet<String>() {{ addAll(primitiveType); add("rw_var"); }});
        firsts.put("Constructor", new HashSet<String>() {{ add("rw_public"); }});

        firsts.put("Body", new HashSet<String>() {{ add("pm_semicolon"); add("pm_par_open"); }});
        firsts.put("FormalArg", new HashSet<String>() {{ addAll(primitiveType); add("id_class"); }});

        firsts.put("LocalVar", new HashSet<String>() {{ add("rw_var"); }});
        firsts.put("Return", new HashSet<String>() {{ add("rw_return"); }});
        firsts.put("Break", new HashSet<String>() {{ add("rw_break"); }});
        firsts.put("If", new HashSet<String>() {{ add("rw_if"); }});
        firsts.put("For", new HashSet<String>() {{ add("rw_for"); }});
        firsts.put("While", new HashSet<String>() {{ add("rw_while"); }});
        firsts.put("Switch", new HashSet<String>() {{ add("rw_switch"); }});
        HashSet<String> expFirst = new HashSet<>();
        HashSet<String> unaryOP = new HashSet<>();

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
        HashSet<String> localVarFirst = new HashSet<>();
        localVarFirst.addAll(primFirst);
        localVarFirst.add("rw_var");

        //      Object
        litFirst.add("rw_null");
        litFirst.add("lit_string");
        firsts.put("Literal", litFirst);
        expFirst.addAll(litFirst);
        //  Access = Primary
        HashSet<String> primaryFirst = new HashSet<>();
        //      THis
        primaryFirst.add("rw_this");
        //      Constructor
        primaryFirst.add("rw_new");

        //      PExpression
        primaryFirst.add("pm_par_open");
        //      VarMet
        primaryFirst.add("id_met_var");

        expFirst.addAll(primaryFirst);
        firsts.put("Primary", primaryFirst);
        firsts.put("Access", primaryFirst);



        HashSet<String> operandFirst = new HashSet<>();
        operandFirst.addAll(expFirst);
        firsts.put("Operand", operandFirst);

        //UNaryOP
        unaryOP.add("op_add");
        unaryOP.add("op_sub");
        unaryOP.add("op_not");

        firsts.put("UnaryOp", unaryOP);
        expFirst.addAll(unaryOP);
        firsts.put("NonStaticExp", expFirst);
        firsts.put("BasicExpression", expFirst);

        firsts.put("Expression", new HashSet<String>() {{ addAll(expFirst);  add("id_class"); }});
        firsts.put("ComplexExpression", new HashSet<String>() {{ addAll(expFirst);  add("id_class"); }});

        firsts.put("Statement", new HashSet<String>() {{ addAll(expFirst); add("rw_var");addAll(primitiveType); add("id_class"); add("rw_for"); add("rw_return"); add("rw_break"); add("rw_if"); add("rw_while"); add("rw_switch"); add("pm_semicolon"); add("pm_brace_open"); }});

        firsts.put("SwitchStatement", new HashSet<String>() {{ add("rw_case"); add("rw_default"); }});

        firsts.put("AssignmentOp", new HashSet<String>() {{ add("assign"); add("assign_add"); add("assign_sub"); }});

        firsts.put("AccessThis", new HashSet<String>() {{ add("rw_this"); }});
        firsts.put("AccessConstructor", new HashSet<String>() {{ add("rw_new"); }});
        firsts.put("AccessStaticMethod", new HashSet<String>() {{ add("id_class"); }});
        firsts.put("PExpression", new HashSet<String>() {{ add("pm_par_open"); }});

        firsts.put("ActualArgs", new HashSet<String>() {{ add("pm_par_open"); }});

        firsts.put("BinaryOp", new HashSet<String>() {{ add("op_and"); add("op_or"); add("op_equal"); add("op_not_equal");  add("op_greater"); add("op_less"); add("op_greater_equal"); add("op_less_equal"); add("op_add"); add("op_sub"); add("op_mult"); add("op_div"); add("op_mod"); }});

        return firsts;
    }

    static boolean isFirst(String nonTerminal, String lexeme) {
        return firsts.get(nonTerminal).contains(lexeme);
    }

}
