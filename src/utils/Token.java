package utils;

public class Token {

    private String token;
    private String lexeme;
    private int line;

    private int column;

    public Token(String token, String lexeme, int line) {
        this.token = token;
        this.lexeme = lexeme;
        this.line = line;
    }

    public String getToken() {
        return token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return "(" + token + "," + lexeme + "," + line + ")";
    }
}
