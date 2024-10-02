package SyntaxAnalyzerOLD.SyntaxAnalyzer;

import utils.LexicalErrorException;
import utils.SyntaxErrorException;

public interface SyntaxAnalyzer {
    String analyzeSintax() throws LexicalErrorException, SyntaxErrorException;
}
