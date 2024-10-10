package AST.Statements;

import utils.Token;

abstract public class StatementNode {

    Token name;

    public StatementNode(Token name){
        this.name = name;
    }

    public abstract boolean isCorrect();
}
