package AST.Expressions.Access;

import AST.LocalVar;
import SymbolTable.MethodDeclaration;
import SymbolTable.ConstructorDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessNewObject extends AccessMember{

    private ConstructorDeclaration constructor;
    public AccessNewObject(Token name, MemberObjectType type){
        super(name);
        this.type = type;
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if( !SymbolTable.hasClass(type.getToken())) throw new SemanticalErrorException(getName(), "Class "+type.getToken().getLexeme()+" does not exist");
        if(SymbolTable.getClass(type.getToken()).isAbstract()) throw new SemanticalErrorException(getName(), "Class "+type.getToken().getLexeme()+" is abstract and cannot be instantiated");
        ConstructorDeclaration constructor = SymbolTable.findConstructor(this.name,parameters.size());
        if(constructor == null) throw new SemanticalErrorException(getName(), "Constructor "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in class "+type.getToken().getLexeme());
        this.constructor = constructor;
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember hasMember) throws SemanticalErrorException{

    }
    public boolean isStatic(){
        return true;
    }
}
