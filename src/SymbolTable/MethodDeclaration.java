package SymbolTable;

import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.HashMap;

public class MethodDeclaration {

    private Token name;
    private MemberType returnType;
    private HashMap<String, ParameterDeclaration> parameters = new HashMap<>();

    public MethodDeclaration(Token name, MemberType returnType){
        this.name = name;
        this.returnType = returnType;
    }

    public Token getName(){
        return this.name;
    }


    public String toString(){
        return "[" + name.getLexeme() + "("+ parameters +") : " + returnType.getName()+"]";
    }

    public void addParameter(Token argName, Token argType) {

        parameters.put(argName.getLexeme(), new ParameterDeclaration(argName, SymbolTable.decideType(argType)));

    }
}
