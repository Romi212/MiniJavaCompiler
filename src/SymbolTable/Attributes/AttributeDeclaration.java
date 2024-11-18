package SymbolTable.Attributes;

import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AttributeDeclaration extends MemberDeclaration {

    private Token name;
    private MemberType type;

    private  String label;

    private boolean isCovered = false;

    private int position = -1;

    public AttributeDeclaration(Token name, MemberType type){
        this.name = name;
        this.type = type;
        this.isStatic = false;
    }

    public void setCovered(boolean isCovered){
        this.isCovered = isCovered;
    }

    public boolean isCovered(){
        return isCovered;
    }

    public Token getName(){
        return this.name;
    }
    public MemberType getType(){
        return this.type;
    }
    public String toString(){
        return "[" + this.name.getLexeme() + " (" + this.type.getName()+")]";
    }

    public boolean isCorrectlyDeclared() throws SemanticalErrorException {
        return type.isCorrect();
    }

    public void generate() {


    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setStaticLabel(String staticLabel) {
        this.label = staticLabel;
    }

    public String getLabel(){
        return label;
    }
}
