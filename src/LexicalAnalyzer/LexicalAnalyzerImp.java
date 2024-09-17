package LexicalAnalyzer;

import utils.LexicalErrorException;
import utils.Token;
import utils.sourcemanager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzerImp implements LexicalAnalyzer {

    private String lexeme;
    private char currentChar;
    private SourceManager sourceManager;

    //private ReservedWords reservedWords;

    public LexicalAnalyzerImp(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        currentChar = ' ';
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
                return possibleFloat();
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
        int column = sourceManager.getLineIndexNumber();
        currentChar = sourceManager.getNextChar();
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), column,sourceManager.getCurrentLine(),"Invalid character.");
    }

    private Token possibleFloat() throws IOException, LexicalErrorException {
        currentChar = sourceManager.getNextChar();
        if((currentChar >= '0' && currentChar <= '9')){
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();
            while (currentChar >= '0' && currentChar <= '9'){
                lexeme += currentChar;
                currentChar = sourceManager.getNextChar();
            }

            //if(lexeme.length()>10) throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid float number, too many digits.");

            if(currentChar == 'e' || currentChar == 'E') {
                lexeme += currentChar;
                return floatExponent();
            }
            else return new Token("lit_float",lexeme,sourceManager.getLineNumber());

        }
        else{
            if(lexeme.charAt(0) == '.'){
                return new Token("pm_period",lexeme,sourceManager.getLineNumber());
            }
            else {
                if(currentChar == 'e' || currentChar == 'E') {
                    lexeme += currentChar;
                    return floatExponent();
                }
            }
        }
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid float number.");

    }

    private Token floatExponent() throws IOException,LexicalErrorException{
        int exponent = 0;
        currentChar = sourceManager.getNextChar();
        if( currentChar == '-' || currentChar == '+'){
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();
        }
        while (currentChar >= '0' && currentChar <= '9'){
            lexeme += currentChar;
            exponent = exponent *10 + (currentChar - '0');
            currentChar = sourceManager.getNextChar();
        }
        if (lexeme.charAt(lexeme.length()-1) == 'e' ||lexeme.charAt(lexeme.length()-1) == '-' ) throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid float number, exponent not found.");

        return new Token("lit_float",lexeme,sourceManager.getLineNumber());

        //throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid float number, exponent too big.");
    }

    private Token possibleString() throws LexicalErrorException, IOException{
        lexeme += currentChar;
        currentChar = sourceManager.getNextChar();
        while (currentChar != '"' && currentChar != SourceManager.END_OF_FILE && currentChar != '\n' && currentChar != '\r') {
            if (currentChar == '\\') {
                lexeme += currentChar;
                currentChar = sourceManager.getNextChar();
            }
            if (currentChar != SourceManager.END_OF_FILE && currentChar != '\n'&&  currentChar != '\r') {
                lexeme += currentChar;
                currentChar = sourceManager.getNextChar();
            }
        }
        if(currentChar == '"'){
            lexeme += currentChar;
            return finalState("lit_string");
        }else if(currentChar == '\n'){
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(),"String was interrupted by a line break.");
        }
        else {
            currentChar = sourceManager.getNextChar();
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(),"String not closed correctly.");
        }

    }

    private Token possibleChar() throws LexicalErrorException, IOException{
        lexeme += currentChar;
        currentChar = sourceManager.getNextChar();
        if (currentChar == '\''){
            currentChar = sourceManager.getNextChar();
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(),sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Char literal empty.");
        }
        if(currentChar == '\\'){
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();
        }

        lexeme += currentChar;
        if(currentChar == SourceManager.END_OF_FILE){
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Char literal not closed correctly.");
        }
        currentChar = sourceManager.getNextChar();

        if(currentChar == '\''){
            lexeme += currentChar;
            return finalState("lit_char");
        }
        else {
            if(currentChar == SourceManager.END_OF_FILE){
                throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Char literal not closed correctly.");
            }
            throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid char literal, more than one char detected.");
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
        if(ReservedWords.isReservedWord(lexeme)) {
            return new Token(ReservedWords.getReservedWord(lexeme),lexeme,sourceManager.getLineNumber());
        }
        else return new Token("id_met_var",lexeme,sourceManager.getLineNumber());
    }

    private Token className() throws IOException {
        do{
            lexeme += currentChar;
            currentChar = sourceManager.getNextChar();

        } while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || currentChar == '_' );
        return new Token("id_class",lexeme,sourceManager.getLineNumber());
    }

    private Token possibleInt() throws LexicalErrorException,IOException{
        int digits = 0;
        do{
            lexeme += currentChar;
            digits++;
            currentChar = sourceManager.getNextChar();

        } while (currentChar >= '0' && currentChar <= '9' && digits < 10);

        if(digits<10) {
            if(currentChar == '.') {
                lexeme += currentChar;
                return possibleFloat();
            }
            if(currentChar == 'e' || currentChar == 'E'){
                lexeme += currentChar;
                return floatExponent();
            }
            return new Token("lit_int",lexeme,sourceManager.getLineNumber());
        }
        else throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Max int length exceeded.");
    }

    private Token possibleOr() throws LexicalErrorException,IOException{

        currentChar = sourceManager.getNextChar();
        if(currentChar == '|'){
            lexeme += currentChar;
            return finalState("op_or");
        }
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(), sourceManager.getLineIndexNumber() , sourceManager.getCurrentLine(), "Invalid character |, expected ||.");
    }

    private Token possibleAnd() throws LexicalErrorException,IOException{
        currentChar = sourceManager.getNextChar();

        if(currentChar == '&'){
            lexeme += currentChar;
            return finalState("op_and");
        }
        throw new LexicalErrorException(lexeme,sourceManager.getLineNumber(),sourceManager.getLineIndexNumber(), sourceManager.getCurrentLine(), "Invalid character &, expected &&.");
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
            currentChar = sourceManager.getNextChar();
            return blockComment();
        }
        return new Token("op_div","/",sourceManager.getLineNumber());
    }

    private Token blockComment() throws IOException,LexicalErrorException{
        while (currentChar != '*' && currentChar != SourceManager.END_OF_FILE){
            currentChar = sourceManager.getNextChar();
        }

        if(currentChar == SourceManager.END_OF_FILE){
            errorState("Comment not closed correctly.");
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
