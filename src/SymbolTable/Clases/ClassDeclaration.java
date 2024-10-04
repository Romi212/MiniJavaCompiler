package SymbolTable.Clases;

import SymbolTable.Attributes.*;
import SymbolTable.ConstructorDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.*;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassDeclaration {

    protected Token name;
    protected Token parent;

    protected boolean visited = false;
    protected boolean isAbstract = false;
    protected boolean hasConstructor =false;
    protected boolean isConsolidated;
    protected HashMap<String, AttributeDeclaration> attributes;
    protected HashMap<String, MethodDeclaration> methods;

    protected HashMap<String, MemberType> parametricTypes;

    protected MethodDeclaration constructor;

    public ClassDeclaration(Token name){
        this.name = name;
        isAbstract = false;
        this.isConsolidated = false;
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
        this.parametricTypes = new HashMap<>();
        constructor = new ConstructorDeclaration(name);
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

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }


    public AttributeDeclaration addAttribute(Token attribute, MemberType typeT) throws SemanticalErrorException{

        if(!attributes.containsKey(attribute.getLexeme())){
            if(typeT.getName().equals("void")) throw new SemanticalErrorException(typeT.getToken(), "Attribute "+attribute.getLexeme()+" cannot be of type void");
            AttributeDeclaration newAtr = new AttributeDeclaration(attribute, typeT);
            this.attributes.put(attribute.getLexeme(), newAtr);
            return newAtr;
        }else throw new SemanticalErrorException(attribute, "Attribute "+attribute.getLexeme()+" in class "+name.getLexeme()+" already exists");
    }

    public MethodDeclaration addMethod(Token method, MemberType returnType) throws  SemanticalErrorException{

        if(!methods.containsKey(method.getLexeme())) {
            MethodDeclaration newMethod = new MethodDeclaration(method, returnType);
            this.methods.put(method.getLexeme(), newMethod);
            return newMethod;

        }
        throw new SemanticalErrorException(method, "Method "+method.getLexeme()+" in class "+name.getLexeme()+" already exists");
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

    public MethodDeclaration addConstructor(Token name) throws SemanticalErrorException{
        if(hasConstructor) throw new SemanticalErrorException(name, "Constructor for class "+this.name.getLexeme()+" already exists");
        if(!name.getLexeme().equals(this.name.getLexeme())) throw new SemanticalErrorException(name, "Constructor for class "+this.name.getLexeme()+" must have the same name as the class");
        this.constructor = new ConstructorDeclaration(name);
        hasConstructor = true;
        return this.constructor;
    }

    public boolean isCorrectlyDeclared() throws  SemanticalErrorException{

        for(HashMap.Entry<String, AttributeDeclaration> entry : attributes.entrySet()){
            if(!entry.getValue().isCorrectlyDeclared()){
                if(!correctParAttribute(entry.getValue())) {

                    throw new SemanticalErrorException(entry.getValue().getType().getToken(), "Attribute "+entry.getValue().getName().getLexeme()+" declared of class "+entry.getValue().getType().getName()+" that doesn't exist nor is a valid Parametric Type");
                }
            }
        }
        for(HashMap.Entry<String, MethodDeclaration> entry : methods.entrySet()){
            if(!entry.getValue().isCorrectlyDeclared(this)) return false;
        }
        return true;
    }

    private boolean correctParAttribute(AttributeDeclaration value) {
        if(!value.isStatic && parametricTypes.containsKey(value.getType().getName())){
            return true;
        }else return false;
    }

    public boolean correctParMethod(MethodDeclaration value) {
        if(parametricTypes.containsKey(value.getReturnType().getName())) {
            return true;
        }else   return false;
    }

    public boolean correctParPar(ParameterDeclaration value) {
        if(parametricTypes.containsKey(value.getType().getName())) {
            return true;
        }else return false;
    }

    public boolean isConsolidated() throws SemanticalErrorException{

        if( !isConsolidated){
            if(!isCorrectlyDeclared()) return false;
            if(visited) throw new SemanticalErrorException(name, "Class "+name.getLexeme()+" has circular inheritance");
            else visited = true;
            SymbolTable.isConsolidated(parent);
            visited = false;
            HashMap<String, AttributeDeclaration> parentAttributes = SymbolTable.getAttributes(parent.getLexeme());
            for(HashMap.Entry<String, AttributeDeclaration> entry : parentAttributes.entrySet()){
                if(attributes.containsKey(entry.getKey())) throw new SemanticalErrorException(attributes.get(entry.getKey()).getName(), "Attribute "+entry.getKey()+" in class "+name.getLexeme()+" already exists in parent class");
                else attributes.put(entry.getKey(), entry.getValue());
            }
            HashMap<String, MethodDeclaration> parentMethods = SymbolTable.getMethods(parent.getLexeme());
            for(HashMap.Entry<String, MethodDeclaration> entry : parentMethods.entrySet()){
                if(methods.containsKey(entry.getKey())) {
                    if(!methods.get(entry.getKey()).sameSignature(entry.getValue()))
                        throw new SemanticalErrorException(methods.get(entry.getKey()).getName(), "Method "+entry.getKey()+" in class "+name.getLexeme()+" cant redefine method with different signature in Parent class");
                }else {
                    if(entry.getValue().isAbstract()&& !isAbstract) throw new SemanticalErrorException(name, "Method "+entry.getKey()+" in class "+name.getLexeme()+" must be implemented!");
                    methods.put(entry.getKey(), entry.getValue());
                }
            }


            isConsolidated = true;
        }
        return true;
    }

    public HashMap<String, AttributeDeclaration> getAttributes() {
        return attributes;
    }

    public HashMap<String,MethodDeclaration> getMethods() {
        return methods;
    }

    public void setConsolidated(boolean b) {
        this.isConsolidated = b;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public MethodDeclaration addAbstractMethod(Token name, MemberType type) throws SemanticalErrorException{
        if(isAbstract){
            MethodDeclaration newMethod = new MethodDeclaration(name, type);
            newMethod.setAbstract(true);

            this.methods.put(name.getLexeme(), newMethod);
            return newMethod;
        }
        throw new SemanticalErrorException(name, "Method "+name.getLexeme()+" in class "+this.name.getLexeme()+" must be declared as abstract");
    }

    protected Token getParent() {
        return parent;
    }

    public void addParametricType(Token genericType) throws SemanticalErrorException {
        if(!parametricTypes.containsKey(genericType.getLexeme())){
            parametricTypes.put(genericType.getLexeme(), new MemberObjectType(genericType));
        }
        else throw new SemanticalErrorException(genericType, "Parametric Type "+genericType.getLexeme()+" already exists in class "+name.getLexeme());
    }

    public void addParametricTypes(ArrayList<Token> generic) throws  SemanticalErrorException{
        for(Token t : generic){
            addParametricType(t);
        }
    }

    public int genericParametersAmount() {
        return parametricTypes.size();
    }
}
