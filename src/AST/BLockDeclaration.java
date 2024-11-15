package AST;

import AST.Statements.StatementNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;
import java.util.HashMap;

public class BLockDeclaration extends StatementNode{

    private ArrayList<StatementNode> statements;

    private boolean cheked;



    public BLockDeclaration(Token name){
        super(name);
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
            statement.setParent(this);
            if(!statement.isCorrect()) throw new SemanticalErrorException(statement.getName(), "Statement is not correct");
        }
        cheked = true;
        SymbolTable.removeLocalVar(getLocalVarSize());
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

    public void generate(){
        for(StatementNode statement : statements){
            statement.generate();
        }

        int localVarSize = getLocalVarSize();
        if (localVarSize > 0) {
            fileWriter.add("FMEM " + localVarSize);

        }

    }


}
