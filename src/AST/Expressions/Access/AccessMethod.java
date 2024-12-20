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

        boolean star = SymbolTable.ImAStar();
        SymbolTable.setStar(true);
        for( ExpressionNode e : parameters){
            e.setParent(parent);

            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            int frame = !method.isStatic() ? 0 : 1;
            if(!type.conformsTo(method.getParameterType(parameters.size() - parameters.indexOf(e)-frame))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        SymbolTable.setStar(star);
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
            e.setParent(parent);
            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            int frame = !m.isStatic() ? 0 : 1;
            if(!type.conformsTo(parentType.transformType(m.getParameterType(parameters.size() - parameters.indexOf(e)-frame)))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
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
            if(hasPrevious) fileWriter.add("DUP ; guardo el this");
            else fileWriter.add("LOAD 3 ; cargamos el this \nDUP ; guardo el this");
            fileWriter.add("LOADREF 0 ; cargamos la VT");
            fileWriter.add("LOADREF "+method.getOffset()+" ; cargamos el label al metodo"+method.getLabel());
        } else{
            if(hasPrevious) fileWriter.add("POP");
            fileWriter.add("PUSH "+method.getLabel()+" ; llamado a metodo estatico");
        }
        fileWriter.add("CALL");

    }
}
