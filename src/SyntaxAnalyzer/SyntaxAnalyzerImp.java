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
        return "Syntax Analyzer";
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
        match("pm_braces_open");
        memberList();
        match("pm_braces_close");
    }

    private void memberList() {
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
            throw new SyntaxErrorException();
        }
    }

}
