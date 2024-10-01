package SymbolTable.Parameters;

import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class ParameterDeclaration {
    private Token name;
    private MemberType type;

    private int position;

    public ParameterDeclaration(Token name, MemberType type, int position){
        this.name = name;
        this.type = type;
        this.position = position;
    }

    public Token getName() {
        return name;
    }

    public String toString(){
        return "[" + this.name.getLexeme() + " (" + this.type.getName()+")]";
    }


    public boolean sameType(ParameterDeclaration parameterDeclaration) {
        return this.type.getName().equals(parameterDeclaration.getType().getName()) && this.name.getLexeme().equals(parameterDeclaration.getName().getLexeme()) && this.position == parameterDeclaration.getPosition();
    }

    private int getPosition() {
        return this.position;
    }

    private MemberType getType() {
        return this.type;
    }

    public boolean isCorrectlyDeclared() throws SemanticalErrorException {
        return type.isCorrect();
    }
}
