package LexicalAnalyzer;

import utils.Exceptions.LexicalErrorException;
import utils.Token;

public interface LexicalAnalyzer {
    Token nextToken() throws LexicalErrorException;
}
