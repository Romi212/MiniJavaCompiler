package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class AccessMethod extends AccessMember{

    MethodDeclaration method;

    public AccessMethod(){
        super(null);
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(method == null) {
            this.method = SymbolTable.findMethod(this.name,parameters.size());
            if(this.method == null){
                throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in current class");
            }

            }
        this.type = method.getType();

        for( ExpressionNode e : parameters){
            e.setParent(parent);

            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            if(!type.conformsTo(method.getParameterType(parameters.indexOf(e)))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        return true;
    }
    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        MemberType parentType = parent.getExpressionType();
        MethodDeclaration m = parentType.hasMethod(this);
        if(m == null) throw new SemanticalErrorException( this.name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" not found in "+parent.getExpressionType().getName());

        if (!m.isPublic()) throw new SemanticalErrorException(this.name, "Method cant be accessed because "+this.name.getLexeme()+" is not public");

        for( ExpressionNode e : parameters){
            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            if(!type.conformsTo(parentType.transformType(m.getParameterType(parameters.size() - parameters.indexOf(e)-1)))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        this.method = m;
        this.type = parentType.transformType(method.getType());
    }
    public boolean isStatement(){
        return true;
    }

    public boolean isStatic(){
        return method.isStatic();
    }

    public void generate(){
        if( !method.getType().isVoid()){
            fileWriter.add("RMEM 1 ; dejo espacio para el return");
            if(hasPrevious) fileWriter.add("SWAP ; volvemos a dejar el label abajo");
        }
        for( ExpressionNode e : parameters){
            e.generate();
            if(hasPrevious) fileWriter.add("SWAP ; seguimos dejando la ref abajo");
        }
        if(!method.isStatic()){
            fileWriter.add("DUP ; guardo el this");
            fileWriter.add("LOADREF 0 ; cargamos la VT");
            fileWriter.add("LOADREF "+method.getOffset()+" ; cargamos el label al metodo"+method.getLabel());
        } else{
            fileWriter.add("PUSH "+method.getLabel()+" ; llamado a metodo estatico");
        }
        fileWriter.add("CALL");
    }
}
