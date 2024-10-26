package AST;

import AST.Statements.StatementNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class BLockDeclaration extends StatementNode{

    private ArrayList<StatementNode> statements;

    private boolean cheked;



    public BLockDeclaration(){
        super(null);
        this.statements = new ArrayList<>();
        cheked = false;
    }

    public void addStatement(StatementNode statement){
        this.statements.add(statement);
        statement.setParent(this);
    }

    public ArrayList<StatementNode> getStatements(){
        return this.statements;
    }


    @Override
    public boolean isCorrect() throws CompilerException {
        if(cheked){
            return true;
        }
        for(StatementNode statement : statements){
            if(!statement.isCorrect()){
                return false;
            }
        }
        cheked = true;
        return true;
    }

    public String toString(){
        String result = "BlockDeclaration{ \n";
        for(StatementNode statement : statements){
            result += statement.toString()+ "\n";
        }
        result += "}";
        return result;
    }

    public boolean isBreakable(){
        return parent!= null && parent.isBreakable();
    }
}
