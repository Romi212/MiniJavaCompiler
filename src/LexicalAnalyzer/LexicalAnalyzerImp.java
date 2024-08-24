package LexicalAnalyzer;

import utils.Token;
import utils.sourcemanager.SourceManager;

public class LexicalAnalyzerImp implements LexicalAnalyzer {

    private String lexeme;
    private char currentChar;
    private SourceManager sourceManager;

    public LexicalAnalyzerImp(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
    }

    @Override
    public Token nextToken(){
        return new Token("EOF", "Prueba", 0);
    }
}
