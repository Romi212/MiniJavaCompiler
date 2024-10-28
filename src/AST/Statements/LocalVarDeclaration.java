package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.LocalVar;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class LocalVarDeclaration extends StatementNode{

    private MemberType type;
    private ExpressionNode initialization;

    public LocalVarDeclaration(Token var) {
        super(var);

    }

    public void setExpression(ExpressionNode expression){
        this.initialization = expression;
        expression.setParent(this);
    }

    @Override
    public boolean isCorrect() throws CompilerException {

        if( initialization == null) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" must be initialized");
        boolean isCorrect = initialization.isCorrect();
        MemberType type = initialization.getExpressionType();
        this.type = type;
        if(type == null || type.conformsTo(null)) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" cant be initialized with null");

        if(type.isVoid()) throw new SemanticalErrorException(this.name, "Local variable declared 'var' "+this.name.getLexeme()+" cant be initialized with void");
        System.out.println("Adding local var "+this.name.getLexeme() + this.name.getLine() + parent);
        if(SymbolTable.isParameter(name)) throw new SemanticalErrorException(name,"Variable name is already used as a parameter");
        parent.addLocalVar(new LocalVar(this.name, type));

        return isCorrect;
    }

    public String toString(){
        String toReturn = "var "+ this.name;
        if(initialization != null){
            toReturn += " = " + initialization.toString();
        }
        return toReturn+";";
    }
}
