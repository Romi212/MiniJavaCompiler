package AST;

import AST.Statements.StatementNode;

import java.util.ArrayList;

public class BLockDeclaration extends StatementNode{

    private ArrayList<StatementNode> statements;

    public BLockDeclaration(){
        super(null);
        this.statements = new ArrayList<>();
    }

    public void addStatement(StatementNode statement){
        this.statements.add(statement);
    }

    public ArrayList<StatementNode> getStatements(){
        return this.statements;
    }

    @Override
    public boolean isCorrect() {
        for(StatementNode statement : statements){
            if(!statement.isCorrect()){
                return false;
            }
        }
        return true;
    }
}
