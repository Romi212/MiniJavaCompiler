package LexicalAnalyzer;

import java.util.HashMap;

public class ReservedWords {

    private HashMap<String, String> reservedWords;

    public ReservedWords(){
        reservedWords = new HashMap<>();
        reservedWords.put("class", "rw_class");
        reservedWords.put("extends", "rw_extends");
        reservedWords.put("public", "rw_public");
        reservedWords.put("static", "rw_static");
        reservedWords.put("void", "rw_void");
        reservedWords.put("boolean", "rw_boolean");
        reservedWords.put("char", "rw_char");
        reservedWords.put("int", "rw_int");
        reservedWords.put("if", "rw_if");
        reservedWords.put("else", "rw_else");
        reservedWords.put("while", "rw_while");
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

    }

    public boolean isReservedWord(String word){
        return reservedWords.containsKey(word);
    }

    public String getReservedWord(String word){
        return reservedWords.get(word);
    }
}
