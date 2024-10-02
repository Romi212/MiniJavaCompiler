package SymbolTable;

import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public abstract class MemberDeclaration {
    public Token visibility;
    public boolean isStatic;

    public void setVisibility(Token visibility)throws  SemanticalErrorException{
        this.visibility = visibility;
    }

    public void setStatic(boolean isStatic){
        this.isStatic = isStatic;
    }

    public Token getVisibility(){
        return this.visibility;
    }

    public boolean isStatic(){
        return this.isStatic;
    }

    public abstract boolean isCorrectlyDeclared() throws SemanticalErrorException;
}
