package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.LocalVar;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;

public class DeclarationStatement extends StatementNode{

    private MemberType type;
    private ArrayList<Token> variables;
    private ExpressionNode expression;

    private ArrayList<LocalVar> localVars;

    public DeclarationStatement(Token op) {
        super(op);
        this.variables = new ArrayList<>();
        this.localVars = new ArrayList<>();
    }

    public void setType(MemberType type){
        this.type = type;
    }

    public void addVariable(Token variable){
        variables.add(variable);
    }

    public void setExpression(ExpressionNode expression){
        this.expression = expression;
        expression.setParent(this);
    }
    @Override
    public boolean isCorrect() throws CompilerException {


        if(expression!= null) {

            expression.setParent(parent);
            if(!expression.isCorrect()) throw new SemanticalErrorException(expression.getName(),"Expression is not correct");
            if(!expression.getExpressionType().conformsTo(type)) throw new SemanticalErrorException(expression.getName(),"Expression does not conform to declared type");
        }
        if(type.isVoid()) throw new SemanticalErrorException(this.name, "Local variables cant be initialized with void");

        for(Token t : variables){
            LocalVar localVar = new LocalVar(t, type);
            if(!type.isCorrect()) throw new SemanticalErrorException(t,"Type is not correct");
            if(SymbolTable.isParameter(t)) throw new SemanticalErrorException(t,"Variable name is already used as a parameter");
            localVars.add(localVar);
            parent.addLocalVar(localVar);
        }
        return true;
    }


    public void generate()  {
        if(expression != null){
            expression.generate();

        }
        for(LocalVar localVar : localVars){
            localVar.setOffset(SymbolTable.getLocalVarSize());
            SymbolTable.addLocalVar();

            fileWriter.add("RMEM 1 ; Assignment expression");
            if(expression!= null){
                fileWriter.add("SWAP");
                fileWriter.add("DUP");
            }

            fileWriter.add("STORE "+localVar.getOffset()+" ; guardamos el valor en la var local "+localVar.getName());
        }
        if(expression != null) fileWriter.add("POP ; sacamos el valor de la expresion de la pila");

    }
    public String toString(){
        String toReturn = "";
        if(type != null){
            toReturn += type.getToken().getLexeme();
        }
        for(Token t : variables){
            toReturn += t.getLexeme() + " ";
        }
        if(expression != null)  toReturn += "= " + expression.toString() + ";";
        return toReturn;
    }
}
