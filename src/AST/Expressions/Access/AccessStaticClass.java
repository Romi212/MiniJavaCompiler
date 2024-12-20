package AST.Expressions.Access;

import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessStaticClass extends AccessMember{

    public AccessStaticClass(Token name){
        super(name);
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        return SymbolTable.hasClass(name);
    }

    @Override
    public MemberType getExpressionType() {
        return new MemberObjectType(name);
    }

    public String toString(){
        return getName().getLexeme();
    }

    @Override
    public void setMember(AccessMember hasMember) {

    }

    public boolean isStatic(){
        return true;
    }

    public boolean isStaticClass(){
        return true;
    }

    public void generate(){

    }
}
