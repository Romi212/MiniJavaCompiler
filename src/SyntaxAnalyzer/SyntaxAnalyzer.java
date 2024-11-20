package SyntaxAnalyzer;

import utils.Exceptions.CompilerException;
import utils.Exceptions.LexicalErrorException;
import utils.Exceptions.SyntaxErrorException;

public interface SyntaxAnalyzer {
    String analyzeSintax(int stage) throws CompilerException;
}
