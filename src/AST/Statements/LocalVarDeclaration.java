package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.LocalVar;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class LocalVarDeclaration extends StatementNode{

    private MemberType type;
    private ExpressionNode initialization;

    public LocalVarDeclaration(Token var) {
        super(var);

    }

    public void setExpression(ExpressionNode expression){
        this.initialization = expression;
    }

    @Override
    public boolean isCorrect() throws CompilerException {

        if( initialization == null) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" must be initialized");
        initialization.setParent(parent);
        if(!initialization.isCorrect()) throw new SemanticalErrorException(initialization.getName(), "Local variable declared 'var' "+this.name.getLexeme()+" initialization is not correct");
        MemberType type = initialization.getExpressionType();
        if(!type.isCorrect()) throw new SemanticalErrorException(initialization.getName(), "Local variable declared 'var' "+this.name.getLexeme()+" initialization is not correct");
        this.type = type;

        if(type == null || type.conformsTo((MemberType) null)) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" cant be initialized with null");

        if(type.isVoid()) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" cant be initialized with void");
        //System.out.println("Adding local var "+this.name.getLexeme() + this.name.getLine() + parent);
        if(SymbolTable.isParameter(name)) throw new SemanticalErrorException(name,"Variable name is already used as a parameter");
        parent.addLocalVar(new LocalVar(this.name, type));

        return true;
    }

    public String toString(){
        String toReturn = "var "+ this.name;
        if(initialization != null){
            toReturn += " = " + initialization.toString();
        }
        return toReturn+";";
    }

    public void generate(){
        fileWriter.add("RMEM 1 ; Assignment expression");
        initialization.generate();
        fileWriter.add("STORE 0 ; guardamos el valor en la var local "+this.name.getLexeme());

    }
}
