package AST.Statements;

import utils.Token;

public class SemicolonNode extends StatementNode{

    public SemicolonNode(Token name){
        super(name);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    public String toString(){
        return "SemicolonNode";
    }
}
