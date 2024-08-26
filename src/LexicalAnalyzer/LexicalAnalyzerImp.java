package LexicalAnalyzer;

import utils.Token;
import utils.sourcemanager.SourceManager;

import java.io.IOException;

public class LexicalAnalyzerImp implements LexicalAnalyzer {

    private String lexeme;
    private char currentChar;
    private SourceManager sourceManager;

    public LexicalAnalyzerImp(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
    }

    @Override
    public Token nextToken(){

        lexeme = "";
        currentChar = ' ';
        return start();
    }

    private Token start(){

        do{
        try {
            currentChar = sourceManager.getNextChar();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }} while (currentChar == ' ' || currentChar == '\n' || currentChar == '\t' || currentChar == '\r');

        switch (currentChar){
            case SourceManager.END_OF_FILE:
                return new Token("EOF",lexeme,sourceManager.getLineNumber());
            case '/':
                lexeme += currentChar;
                return new Token("DIV",lexeme,sourceManager.getLineNumber());
            case '+':
                lexeme += currentChar;
                return new Token("PLUS",lexeme,sourceManager.getLineNumber());
            case '-':
                lexeme += currentChar;
                return new Token("MINUS",lexeme,sourceManager.getLineNumber());
            case '*':
                lexeme += currentChar;
                return new Token("TIMES",lexeme,sourceManager.getLineNumber());

            case '(':
                lexeme += currentChar;
                return new Token("LPAREN",lexeme,sourceManager.getLineNumber());
            case ')':
                lexeme += currentChar;
                return new Token("RPAREN",lexeme,sourceManager.getLineNumber());
            case ';':
                lexeme += currentChar;
                return new Token("SEMICOLON",lexeme,sourceManager.getLineNumber());
            case ':':
                return possibleAssign();
            case '=':
                lexeme += currentChar;
                return new Token("EQ",lexeme,sourceManager.getLineNumber());
            case '<':
                return possibleLess();
            case '>':
                return possibleGreater();
            case '!':
                return possibleNot();
            default:
                return possibleId();
        }



        if(currentChar == '/'){
            return possibleComment();
        }
        lexeme += currentChar;
        return new Token(":D",lexeme,sourceManager.getLineNumber());
    }

    private Token possibleComment() {
        try {
            currentChar = sourceManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(currentChar == '/'){
            return lineComment();
        }
        if(currentChar == '*'){
            return blockComment();
        }
        return new Token("DIV",lexeme,sourceManager.getLineNumber());
    }

    private Token blockComment() {
        do{
            try {
                currentChar = sourceManager.getNextChar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lexeme += currentChar;
        } while (currentChar != '*' && currentChar != SourceManager.END_OF_FILE);

        //TODO: ver esto
        if(currentChar == SourceManager.END_OF_FILE){
            throw new RuntimeException("Comment not closed");
        }
        else return closingBlockComment();
    }

    private Token closingBlockComment() {
        try {
            currentChar = sourceManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(currentChar == '/'){
            return start();
        }
        //TODO: MALLLL
        return blockComment();
    }

    private Token lineComment() {
        do{
            try {
                currentChar = sourceManager.getNextChar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (currentChar != '\n' && currentChar != SourceManager.END_OF_FILE);
        return start();
    }
}
