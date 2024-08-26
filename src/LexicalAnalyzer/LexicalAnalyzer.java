package LexicalAnalyzer;

import utils.LexicalErrorException;
import utils.Token;

public interface LexicalAnalyzer {
    Token nextToken() throws LexicalErrorException;
}
