package SyntaxAnalyzer;

import LexicalAnalyzer.LexicalAnalyzer;
import utils.LexicalErrorException;
import utils.SyntaxErrorException;
import utils.Token;

import java.sql.SQLSyntaxErrorException;

public class SyntaxAnalyzerImp implements SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzerImp(LexicalAnalyzer lexicalAnalyzer){
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    @Override
    public String analyzeSintax() throws LexicalErrorException, SyntaxErrorException{
        getNewToken();
        initial();
        return "[SinErrores]";
    }

    private void getNewToken() throws LexicalErrorException {
        currentToken = lexicalAnalyzer.nextToken();
    }
    private void initial() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("ClassList", currentToken.getToken())){
            classList();
        }
    }

    private void classList() throws SyntaxErrorException, LexicalErrorException {
        if (Firsts.isFirst("Class", currentToken.getToken())) {
            classT();
            classList();
        }
    }

    private void classT() throws SyntaxErrorException, LexicalErrorException {
        match("rw_class");
        match("id_class");
        parents();
        match("pm_brace_open");
        memberList();
        match("pm_brace_close");
    }

    private void memberList() throws  LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Member", currentToken.getToken())){
            member();
            memberList();
        }
        else{
            //TODO Check follows
        }
    }

    private void member() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Declaration", currentToken.getToken())){
            declaration();
            body();
        }
        else if(Firsts.isFirst("Constructor", currentToken.getToken())){
            constructor();
        }
        else{
            //TODO Check follows
        }
    }

    private void constructor() {
    }

    private void body() {
    }

    private void declaration() throws LexicalErrorException, SyntaxErrorException {
        staticT();
        memberType();
        match("id_met_var");
    }

    private void memberType() throws   LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Type", currentToken.getToken())){
            type();
        }
        else {
            match("id_void");

        }
    }

    private void type() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            primitiveType();
        }
        else{
            match("id_class");
        }
    }

    private void primitiveType() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_int")){
            match("rw_int");
        }
        else if(currentToken.getToken().equals("rw_char")){
            match("rw_char");
        }
        else{
            match("rw_boolean");
        }

    }

    private void staticT() throws LexicalErrorException, SyntaxErrorException {
        if (currentToken.getToken().equals("rw_static")) {
            match("rw_static");
        }
        else{
            //TODO Check follows
        }
    }

    private void parents() throws LexicalErrorException, SyntaxErrorException{
        if(currentToken.getToken().equals("rw_extends")){
            match("rw_extends");
            match("id_class");
        }
        else{
            //TODO: check follows
        }
    }

    private void match(String terminal) throws LexicalErrorException, SyntaxErrorException {
        if (currentToken.getToken().equals(terminal)) {
            getNewToken();
        }
        else{
            throw new SyntaxErrorException(terminal, currentToken.getToken(), currentToken.getLine(), currentToken.getColumn());
        }
    }

}
