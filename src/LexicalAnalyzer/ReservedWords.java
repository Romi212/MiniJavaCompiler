package LexicalAnalyzer;

import java.util.HashMap;

public class ReservedWords {

    static HashMap<String, String> reservedWords = createMap();

    static HashMap<String,String> createMap(){
        HashMap<String, String> reservedWords = new HashMap<>();
        reservedWords.put("class", "rw_class");
        reservedWords.put("extends", "rw_extends");
        reservedWords.put("private", "rw_private");
        reservedWords.put("abstract", "rw_abstract");
        reservedWords.put("public", "rw_public");
        reservedWords.put("static", "rw_static");
        reservedWords.put("void", "rw_void");
        reservedWords.put("boolean", "rw_boolean");
        reservedWords.put("char", "rw_char");
        reservedWords.put("int", "rw_int");
        reservedWords.put("float", "rw_float");
        reservedWords.put("if", "rw_if");
        reservedWords.put("else", "rw_else");
        reservedWords.put("while", "rw_while");
        reservedWords.put("for", "rw_for");
        reservedWords.put("return", "rw_return");
        reservedWords.put("var", "rw_var");
        reservedWords.put("switch", "rw_switch");
        reservedWords.put("case", "rw_case");
        reservedWords.put("break", "rw_break");
        reservedWords.put("this", "rw_this");
        reservedWords.put("new", "rw_new");
        reservedWords.put("null", "rw_null");
        reservedWords.put("true", "rw_true");
        reservedWords.put("false", "rw_false");
        reservedWords.put("default", "rw_default");
        return reservedWords;

    }

    static boolean isReservedWord(String word){
        return reservedWords.containsKey(word);
    }

    static String getReservedWord(String word){
        return reservedWords.get(word);
    }
}
