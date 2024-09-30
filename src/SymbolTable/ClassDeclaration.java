package SymbolTable;

import SymbolTable.Attributes.*;
import SymbolTable.Types.*;
import utils.Token;

import java.util.HashMap;

public class ClassDeclaration {

    private Token name;
    private Token parent;
    private boolean isAbstract;

    private boolean isConsolidated;
    private HashMap<String, AttributeDeclaration> attributes;
    private HashMap<String,MethodDeclaration> methods;

    private MethodDeclaration constructor;

    public ClassDeclaration(Token name){
        this.name = name;
        isAbstract = false;
        this.isConsolidated = false;
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public ClassDeclaration(){
        this.isConsolidated = false;
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public void setName(Token name) {
        this.name = name;
    }

    public void setParent(Token parent) {
        this.parent = parent;
    }


    public void addAttribute(Token attribute, Token typeT){

        AttributeDeclaration newAtr = new AttributeDeclaration(attribute, SymbolTable.decideType(typeT));
        this.attributes.put(attribute.getLexeme(), newAtr);
    }

    public MethodDeclaration addMethod(Token method, Token returnType){

        MethodDeclaration newMethod = new MethodDeclaration(method, SymbolTable.decideType(returnType));

        this.methods.put(method.getLexeme(), newMethod);

        return newMethod;
    }

    public Token getName(){
        return this.name;
    }

    public String toString(){
        String p = "";
        if(parent != null) p = this.parent.getLexeme();
        else p = "Nill";
        return "Class: "+this.name.getLexeme()+"\n extends: "+p+"\n Attributes: "+this.attributes.toString()+"\n Methods: "+this.methods.toString()+"\n";
    }

    public void makeAbstract() {
        this.isAbstract = true;
    }

    public MethodDeclaration addConstructor(Token name) {
        this.constructor = new MethodDeclaration(name, new VoidType(name));
        return this.constructor;
    }
}
