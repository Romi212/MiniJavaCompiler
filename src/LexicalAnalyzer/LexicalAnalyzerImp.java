package LexicalAnalyzer;

import utils.LexicalErrorException;
import utils.Token;
import utils.sourcemanager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzerImp implements LexicalAnalyzer {

    private String lexeme;
    private char currentChar;
    private SourceManager sourceManager;

    private ReservedWords reservedWords;

    public LexicalAnalyzerImp(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        currentChar = ' ';
        reservedWords = new ReservedWords();
    }

    @Override
    public Token nextToken() throws LexicalErrorException {

        lexeme = "";
        Token token = null;
        try {
            token = start();
        } catch (IOException e) {
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(),"Error reading file");

        }
        return token;
    }

    private Token start() throws LexicalErrorException, IOException {

        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\t' || currentChar == '\r'){
                currentChar = sourceManager.getNextChar();
        } 

        switch (currentChar){
            case SourceManager.END_OF_FILE:
                return new Token("EOF",lexeme,sourceManager.getLineNumber());
            case '/':
                return possibleComment();
            case '+':
                lexeme += currentChar;
                return plusSign();
            case '-':
                lexeme += currentChar;
                return lessSign();
            case '*':
                lexeme += currentChar;
                return finalState("op_mult");
            case '%':
                lexeme += currentChar;
                return finalState("op_mod");

            case '(':
                lexeme += currentChar;
                return finalState("pm_par_open");
            case ')':
                lexeme += currentChar;
                return finalState("pm_par_close");

            case '{':
                lexeme += currentChar;
                return finalState("pm_brace_open");

            case '}':
                lexeme += currentChar;
                return finalState("pm_brace_close");
            case ';':
                lexeme += currentChar;
                return finalState("pm_semicolon");
            case ':':
                return finalState("pm_colon");
            case ',':
                lexeme += currentChar;
                return finalState("pm_comma");
            case '.':
                lexeme += currentChar;
                return finalState("pm_period");
            case '=':
                lexeme += currentChar;
                return firstEqual();
            case '<':
                return possibleLess();
            case '>':
                return possibleGreater();
            case '!':
                return possibleNot();
            case '&':
                lexeme += currentChar;
                return possibleAnd();
            case '|':
                lexeme += currentChar;
                return possibleOr();
            case '\'':
                return possibleChar();                
            case '"':
                return possibleString();
                
            default:
                if (currentChar >= '0' && currentChar <= '9') {
                    return possibleInt();
                } else {
                    if ((currentChar >= 'a' && currentChar <= 'z') ) {
                        return possibleId();
                    } else if (currentChar >= 'A' && currentChar <= 'Z'){
                        return className();
                    }
                }
        }

        lexeme += currentChar;
        currentChar = sourceManager.getNextChar();
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(),sourceManager.getCurrentLine(),"Invalid character");
    }

    private Token possibleString() throws LexicalErrorException, IOException{
        lexeme += currentChar;
        currentChar = sourceManager.getNextChar();
        while (currentChar != '"' && currentChar != SourceManager.END_OF_FILE && currentChar != '\n'&& currentChar != '\t' && currentChar != '\r') {
            if (currentChar == '\\') {
                lexeme += currentChar;
                currentChar = sourceManager.getNextChar();
            }
            if (currentChar != SourceManager.END_OF_FILE) {
                lexeme += currentChar;
                currentChar = sourceManager.getNextChar();
            }
        }
        if(currentChar == '"'){
            lexeme += currentChar;
            return finalState("lit_string");
        }else {
            currentChar = sourceManager.getNextChar();
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(),"String not closed");
        }

    }

    private Token possibleChar() throws LexicalErrorException, IOException{
        currentChar = sourceManager.getNextChar();
        if (currentChar == '\''){
            currentChar = sourceManager.getNextChar();
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(),sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "invalid character");
        }
        if(currentChar == '\\'){
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();
        }

        lexeme += currentChar;
        if(currentChar == SourceManager.END_OF_FILE){
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "expected a character but found EOF");
        }
        currentChar = sourceManager.getNextChar();

        if(currentChar == '\''){
            return finalState("lit_char");
        }
        else {
            currentChar = sourceManager.getNextChar();
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "expected a character but found more than one");
        }
    }

    private Token possibleId() throws IOException {
        do{
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();

        } while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || currentChar == '_' );
        return checkReservedWord();
    }

    private Token checkReservedWord() {
        if(reservedWords.isReservedWord(lexeme)) {
            return new Token(reservedWords.getReservedWord(lexeme),lexeme,sourceManager.getLineNumber());
        }
        else return new Token("id_met_var",lexeme,sourceManager.getLineNumber());
    }

    private Token className() throws IOException {
        do{
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();

        } while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || currentChar == '_' );
        return finalState("id_class");
    }

    private Token possibleInt() throws LexicalErrorException,IOException{
        int digits = 0;
        do{
            lexeme += currentChar;
            digits++;
            currentChar = sourceManager.getNextChar();

        } while (currentChar >= '0' && currentChar <= '9' && digits < 10);
        if(digits<10) return new Token("lit_int",lexeme,sourceManager.getLineNumber());
        else throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Integer too long");
    }

    private Token possibleOr() throws LexicalErrorException,IOException{

        currentChar = sourceManager.getNextChar();
        if(currentChar == '|'){
            lexeme += currentChar;
            return finalState("op_or");
        }
        currentChar = sourceManager.getNextChar();
        errorState("expected || but found |");
        return null;
    }

    private Token possibleAnd() throws LexicalErrorException,IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '&'){
            lexeme += currentChar;
            return finalState("op_and");
        }
        currentChar = sourceManager.getNextChar();
        errorState("expected && but found &");
        return null;
    }

    private Token possibleNot() throws IOException {

        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("op_not_equal");
        }
        return new Token("op_not",lexeme,sourceManager.getLineNumber());
    }

    private Token possibleGreater() throws IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("op_greater_equal");
        }
        return new Token("op_greater",lexeme,sourceManager.getLineNumber());
    }

    private Token possibleLess() throws IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("op_less_equal");
        }
        return new Token("op_less",lexeme,sourceManager.getLineNumber());
    }

    private Token firstEqual() throws IOException {
        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("op_equal");
        }
        return new Token("assign",lexeme,sourceManager.getLineNumber());
    }

    private Token finalState(String token) throws IOException {

        currentChar = sourceManager.getNextChar();
        return new Token(token,lexeme,sourceManager.getLineNumber());
    }

    private Token lessSign() throws IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("assign_sub");
        }
        return new Token("op_sub",lexeme,sourceManager.getLineNumber());
    }

    private Token plusSign() throws IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '='){
            lexeme += currentChar;
            return finalState("assign_add");
        }
        return new Token("op_add",lexeme,sourceManager.getLineNumber());
    }


    private Token possibleComment() throws IOException,LexicalErrorException{

        currentChar = sourceManager.getNextChar();


        if(currentChar == '/'){
            return lineComment();
        }
        if(currentChar == '*'){
            return blockComment();
        }
        return new Token("op_div","/",sourceManager.getLineNumber());
    }

    private Token blockComment() throws IOException,LexicalErrorException{
        do{
            currentChar = sourceManager.getNextChar();
        } while (currentChar != '*' && currentChar != SourceManager.END_OF_FILE);

        if(currentChar == SourceManager.END_OF_FILE){
            errorState("Comment not closed");
        }
        return closingBlockComment();
    }

    private Token closingBlockComment() throws  IOException,LexicalErrorException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '/'){
            currentChar = sourceManager.getNextChar();
            return start();
        }
        else return blockComment();

    }

    private Token lineComment() throws IOException,LexicalErrorException{
        do{
            currentChar = sourceManager.getNextChar();

        } while (currentChar != '\n' && currentChar != SourceManager.END_OF_FILE);
        return start();
    }

    private void errorState(String message) throws LexicalErrorException {
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), message);
    }
}
