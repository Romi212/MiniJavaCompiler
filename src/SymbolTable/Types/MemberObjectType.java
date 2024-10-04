package SymbolTable.Types;

import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.SymbolTable;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberObjectType extends MemberType{

    private ArrayList<MemberObjectType> attributes;

    public MemberObjectType(Token name){
        this.name = name;
        attributes = new ArrayList<>();
    }

    public boolean isCorrect() throws SemanticalErrorException {
        if(SymbolTable.hasClass(this.name)){
            ClassDeclaration classType = SymbolTable.getClass(this.name);
            if(classType.genericParametersAmount() == attributes.size()){
                for (MemberObjectType attribute : attributes) {
                    if(!SymbolTable.hasClass(attribute.getToken())) throw new SemanticalErrorException(attribute.getToken(), "Class "+attribute.getToken().getLexeme()+" does not exist or is not valid generic type to instantiate the generic types of "+name.getLexeme());
                }
                return true;
            } else {
                throw new SemanticalErrorException(name, "Class "+name.getLexeme()+" has "+classType.genericParametersAmount()+" generic parameters, but "+attributes.size()+" were given");
            }
        }
        return false;
    }

    public void addAttribute(MemberObjectType attribute) {
        attributes.add(attribute);
    }

    public ArrayList<MemberObjectType> getAttributes() {
        return attributes;
    }

    public void addParametricTypes(ArrayList<MemberObjectType> parametricTypes) {
        for (MemberObjectType attribute : parametricTypes) {
            attributes.add(attribute);
        }
    }
    public void addParametricTokens(ArrayList<Token> parametricTypes) {
        for (Token type : parametricTypes) {
            attributes.add(new MemberObjectType(type));
        }
    }
}
