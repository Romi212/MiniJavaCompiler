package SymbolTable.Clases;

import AST.Expressions.Access.AccessMember;
import AST.LocalVar;
import AST.Statements.StatementNode;
import SymbolTable.Attributes.*;
import SymbolTable.ConstructorDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.*;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import SymbolTable.MemberDeclaration;

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

    protected ArrayList<MemberObjectType> orderedParametricTypes;
    protected HashMap<String, ConstructorDeclaration> constructors;

    protected MethodDeclaration currentMethod;
    protected MemberDeclaration currentMember;

    public ClassDeclaration(Token name){
        this.name = name;
        isAbstract = false;
        this.isConsolidated = false;
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
        this.parametricTypes = new HashMap<>();
        constructors = new HashMap<>();
        constructors.put("default", new ConstructorDeclaration(name));
        orderedParametricTypes = new ArrayList<>();
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
///TODO CHANGE THIS DELETE ARREGLAR BOOO

        if(!methods.containsKey(method.getLexeme())) {
            MethodDeclaration newMethod = new MethodDeclaration(method, returnType);
            this.methods.put(method.getLexeme(), newMethod);
            return newMethod;

        }
        throw new SemanticalErrorException(method, "Method "+method.getLexeme()+" in class "+name.getLexeme()+" already exists");
    }

    public MethodDeclaration addMethod(MethodDeclaration newMethod) throws  SemanticalErrorException{

        String key = "#"+newMethod.getParametersSize()+"#"+newMethod.getName().getLexeme();

        if(!methods.containsKey(key)) {

            currentMethod = newMethod;
            this.methods.put(key, newMethod);
            return newMethod;

        }
        throw new SemanticalErrorException(newMethod.getName(), "Method "+ newMethod.getName().getLexeme()+" in class "+name.getLexeme()+" already exists with same amount of parameters");
    }

    public void addStatement(StatementNode statement){
        this.currentMethod.addStatement(statement);
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


    public MethodDeclaration addConstructor(ConstructorDeclaration name) throws SemanticalErrorException{

        if(!name.getName().getLexeme().equals(this.name.getLexeme())) throw new SemanticalErrorException(name.getName(), "Constructor for class "+this.name.getLexeme()+" must have the same name as the class");
        String key = "#"+name.getParametersSize()+"#";
        if(constructors.containsKey(key)) throw new SemanticalErrorException(name.getName(), "Constructor for class "+this.name.getLexeme()+" already exists with same amount of parameters");
        else{
            if(name.getParametersSize()== 0) constructors.remove("default");
            constructors.put(key, name);
        }
        hasConstructor = true;
        return name;
    }

    public boolean isCorrectlyDeclared() throws CompilerException {


        for(HashMap.Entry<String, AttributeDeclaration> entry : attributes.entrySet()){
            currentMember = entry.getValue();
            if(!entry.getValue().isCorrectlyDeclared()){
                    throw new SemanticalErrorException(entry.getValue().getType().getToken(), "Attribute "+entry.getValue().getName().getLexeme()+" declared of class "+entry.getValue().getType().getName()+" that doesn't exist nor is a valid Parametric Type");
            }
        }
        for(HashMap.Entry<String, ConstructorDeclaration> entry : constructors.entrySet()){
            currentMember = entry.getValue();
            if(!entry.getValue().isCorrectlyDeclared()) return false;
        }
        for(HashMap.Entry<String, MethodDeclaration> entry : methods.entrySet()){
            currentMember = entry.getValue();
            if(!entry.getValue().isCorrectlyDeclared()) return false;
        }
        return true;
    }



    public boolean isConsolidated() throws CompilerException{

        if( !isConsolidated){
            if(!isCorrectlyDeclared()) return false;
            if(visited) throw new SemanticalErrorException(name, "Class "+name.getLexeme()+" has circular inheritance");
            else visited = true;
            SymbolTable.isConsolidated(parent);
            visited = false;
            HashMap<String, AttributeDeclaration> parentAttributes = SymbolTable.getAttributes(parent.getLexeme());
            HashMap<String, AttributeDeclaration> toAdd = new HashMap<>();
            for(HashMap.Entry<String, AttributeDeclaration> entry : parentAttributes.entrySet()){
                if(entry.getKey().charAt(0)== '#') {
                    if(attributes.containsKey(entry.getValue().getName().getLexeme())) toAdd.put("#"+entry.getKey(), entry.getValue());
                    else toAdd.put(entry.getKey(), entry.getValue());
                }else{
                    if(attributes.containsKey(entry.getKey())) toAdd.put("#"+entry.getKey(), entry.getValue());
                    else toAdd.put(entry.getKey(), entry.getValue());
                }

            }
            attributes.putAll(toAdd);
            HashMap<String, MethodDeclaration> parentMethods = SymbolTable.getMethods(parent.getLexeme());
            for(HashMap.Entry<String, MethodDeclaration> entry : parentMethods.entrySet()){
                if(methods.containsKey(entry.getKey())) {
                    if(!methods.get(entry.getKey()).sameSignature(entry.getValue()))
                        throw new SemanticalErrorException(methods.get(entry.getKey()).getName(), "Method "+entry.getValue().getName().getLexeme()+" in class "+name.getLexeme()+" cant redefine method with different signature in Parent class");
                }else {
                    if(entry.getValue().isAbstract()&& !isAbstract) throw new SemanticalErrorException(name, "Method "+entry.getValue().getName().getLexeme()+" in class "+name.getLexeme()+" must be implemented!");
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

    public MethodDeclaration addAbstractMethod(MethodDeclaration method) throws SemanticalErrorException{
        if(isAbstract){
            String name = "#"+method.getParametersSize()+"#"+method.getName().getLexeme();
            if(!methods.containsKey(name)){
                this.methods.put(name, method);
                return method;
            }else throw new SemanticalErrorException(method.getName(), "Method "+method.getName().getLexeme()+" in class "+this.name.getLexeme()+" already exists with same amount of parameters");
        }
        throw new SemanticalErrorException(name, "Method "+name.getLexeme()+" in class "+this.name.getLexeme()+" must be declared as abstract");
    }

    protected Token getParent() {
        return parent;
    }

    public void addParametricType(Token genericType) throws SemanticalErrorException {
        if(!parametricTypes.containsKey(genericType.getLexeme())){
            MemberObjectType newType = new MemberObjectType(genericType);
            parametricTypes.put(genericType.getLexeme(), newType);
            orderedParametricTypes.add(newType);
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

    public ArrayList<MemberObjectType> getParametricTypes() {
        return orderedParametricTypes;
    }
    public HashMap<String,MemberType> getParametricTypesSet() {
        return parametricTypes;
    }

    public MemberDeclaration getCurrentMember() {
        return currentMember;
    }

    public void addLocalVar(LocalVar localVar) {
        this.currentMethod.addLocalVar(localVar);
    }

    public boolean isLocalVar(Token name) {
        return this.currentMethod.hasLocalVar(name.getLexeme());
    }

    public boolean isParameter(Token name) {
        return this.currentMethod.hasParameter(name.getLexeme());
    }

    public boolean isAtribute(Token name) {
        return this.attributes.containsKey(name.getLexeme());
    }

    public MemberType visibleVar(Token name) {
        MemberType type = currentMethod.visibleVar(name);
        if(type == null){
            MemberDeclaration m =  attributes.get(name.getLexeme());
            if(m!= null) return m.getType();
        }
        return type;
    }

    public MethodDeclaration findMethod(Token name, int size) {
        String key = "#"+size+"#"+name.getLexeme();
        if(methods.containsKey(key)) return methods.get(key);
        return null;
    }

    public MemberDeclaration getMember(AccessMember link) {
        if(attributes.containsKey(link.getName().getLexeme())) return attributes.get(link.getName().getLexeme());
        return methods.get("#"+link.getParametersSize()+"#"+link.getName().getLexeme());
    }
}
