package AST.Statements;

import utils.Exceptions.CompilerException;
import utils.Token;

abstract public class StatementNode {

    protected Token name;

    public StatementNode(Token name){
        this.name = name;
    }

    public abstract boolean isCorrect() throws CompilerException;


    public Token getName() {
        return name;
    }
}
