package AST;

import AST.Statements.StatementNode;
import utils.Exceptions.CompilerException;

import java.util.ArrayList;
import java.util.HashMap;

public class BLockDeclaration extends StatementNode{

    private ArrayList<StatementNode> statements;

    private HashMap<String, LocalVar> localVars;

    public BLockDeclaration(){
        super(null);
        this.statements = new ArrayList<>();
        this.localVars = new HashMap<>();
    }

    public void addStatement(StatementNode statement){
        this.statements.add(statement);
    }

    public ArrayList<StatementNode> getStatements(){
        return this.statements;
    }

    public void addLocalVar(LocalVar localVar){
        //TODO CHECK REPETIDO
        this.localVars.put(localVar.getName().getLexeme(), localVar);
    }

    public boolean containsLocalVar(String name){
        return this.localVars.containsKey(name);
    }
    @Override
    public boolean isCorrect() throws CompilerException {
        for(StatementNode statement : statements){
            if(!statement.isCorrect()){
                return false;
            }
        }
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
}
