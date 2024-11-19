package SymbolTable.Clases;

import AST.Expressions.Access.AccessMember;
import AST.Expressions.Access.AccessMethod;
import AST.Expressions.Access.AccessVar;
import AST.LocalVar;
import AST.Statements.StatementNode;
import SymbolTable.Attributes.*;
import SymbolTable.ConstructorDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.*;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import SymbolTable.MemberDeclaration;
import utils.fileWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassDeclaration {

    protected Token name;
    protected Token parent;
    protected int dynamicAtt;
    protected boolean visited = false;
    protected boolean isAbstract = false;
    protected boolean hasConstructor =false;
    protected boolean isConsolidated;
    protected int coveredAtt;
    protected HashMap<String, AttributeDeclaration> attributes;
    protected ArrayList<AttributeDeclaration> sortedAttributes;

    protected HashMap<String, AttributeDeclaration> blindAttributes;
    protected HashMap<String, MethodDeclaration> methods;
    protected HashMap<String, MethodDeclaration> ancestorMethods;
    protected ArrayList<MethodDeclaration> sortedMethods;

    protected HashMap<String, MemberType> parametricTypes;

    protected ArrayList<MemberObjectType> orderedParametricTypes;
    protected ArrayList<MemberObjectType> parentGenericInstance;
    protected HashMap<String, ConstructorDeclaration> constructors;

    protected MethodDeclaration currentMethod;
    protected MemberDeclaration currentMember;

    protected HashMap<String,MemberObjectType> instanceGenericTypes;

    public ClassDeclaration(Token name){
        this.name = name;
        isAbstract = false;
        this.isConsolidated = false;
        this.attributes = new HashMap<>();
        this.sortedAttributes = new ArrayList<>();
        this.methods = new HashMap<>();
        this.parametricTypes = new HashMap<>();
        constructors = new HashMap<>();
        constructors.put("default", new ConstructorDeclaration(name));
        orderedParametricTypes = new ArrayList<>();
        instanceGenericTypes = new HashMap<>();
        sortedMethods = new ArrayList<>();
        blindAttributes = new HashMap<>();
        ancestorMethods = new HashMap<>();
        dynamicAtt = 0;
        coveredAtt = 0;
        parentGenericInstance = new ArrayList<>();


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
            if(!newAtr.isStatic()){
                sortedAttributes.add(newAtr);
            }
            return newAtr;
        }else throw new SemanticalErrorException(attribute, "Attribute "+attribute.getLexeme()+" in class "+name.getLexeme()+" already exists");
    }



    public MethodDeclaration addMethod(MethodDeclaration newMethod) throws  SemanticalErrorException{

        String key = "#"+newMethod.getParametersSize()+"#"+newMethod.getName().getLexeme();

        if(!methods.containsKey(key)) {

            currentMethod = newMethod;
            this.methods.put(key, newMethod);
            newMethod.setLabel("lblMet"+newMethod.getParametersSize()+newMethod.getName().getLexeme()+"@"+name.getLexeme());
            if(newMethod.getVisibility().getLexeme().equals("public")) sortedMethods.addFirst(newMethod);
            else sortedMethods.addLast(newMethod);
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
        name.setLabel("lblCtr"+name.getParametersSize()+"@"+this.name.getLexeme());
        if(constructors.containsKey(key)) throw new SemanticalErrorException(name.getName(), "Constructor for class "+this.name.getLexeme()+" already exists with same amount of parameters");
        else{
            if(!hasConstructor) constructors.remove("default");
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

        for (int i = 0; i < sortedMethods.size(); i++) {
            MethodDeclaration method = sortedMethods.get(i);
            currentMethod = method;
            if(!method.isCorrectlyDeclared()) return false;
            //method.setOffset(i);
        }

        if(parametricTypes.size() != orderedParametricTypes.size()) throw new SemanticalErrorException(name, "More than one parametric type declared with same name in class "+name.getLexeme());

        for(MemberObjectType t : orderedParametricTypes){
            if(SymbolTable.hasClass(t.getToken())){ throw new SemanticalErrorException(t.getToken(), "Parametric Type "+t.getName()+" in class "+name.getLexeme()+" is already declared as a class");}
        }

        ClassDeclaration parentC = SymbolTable.getClass(parent);
        if(parentC == null) throw new SemanticalErrorException(parent, "Parent class "+parent.getLexeme()+" doesn't exist");
        ArrayList<MemberObjectType> parentTypes = parentC.getParametricTypes();

        if(parentTypes.size() != parentGenericInstance.size()) throw new SemanticalErrorException(name, "Parent class "+parent.getLexeme()+" has "+parentTypes.size()+" parametric types, but "+name.getLexeme()+" has "+parentGenericInstance.size());
        for(MemberObjectType t: parentGenericInstance){
            if(!SymbolTable.hasClass(t.getToken())){
                if (!parametricTypes.containsKey(t.getName())) throw new SemanticalErrorException(t.getToken(), "Parametric Type instance "+t.getName()+" in class "+name.getLexeme()+" doesn't exist");
            }
            setInstanceType(parentTypes.get(parentGenericInstance.indexOf(t)), t);
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

            //HashMap<String,MemberType> parentInstances = SymbolTable.getClass(parent).getParametricTypesSet();

            HashMap<String, AttributeDeclaration> parentAttributes = SymbolTable.getAttributes(parent.getLexeme());
            HashMap<String, AttributeDeclaration> toAdd = new HashMap<>();
            HashMap<String, AttributeDeclaration> toAddPrivate = new HashMap<>();
            int position = 0;
            int covered = 0;
            for(HashMap.Entry<String, AttributeDeclaration> entry : parentAttributes.entrySet()){


                if(attributes.containsKey(entry.getValue().getName().getLexeme())){
                    MemberType type = entry.getValue().getType();
                    if(instanceGenericTypes.containsKey(type.getName())){
                        type = instanceGenericTypes.get(type.getName());
                    }
                    AttributeDeclaration coveredAtt = new AttributeDeclaration(entry.getValue().getName(), type);
                    coveredAtt.setCovered(true);
                    if(entry.getValue().isPublic())toAdd.put("#"+entry.getKey(), coveredAtt);
                    else toAddPrivate.put("#"+entry.getKey(), coveredAtt);
                    coveredAtt.setPosition(covered++);
                    attributes.get(entry.getValue().getName().getLexeme()).setPosition(entry.getValue().getPosition());
                    if(sortedAttributes.remove(attributes.get(entry.getValue().getName().getLexeme()))) if(!entry.getValue().isStatic()) position++;
                }else{
                    MemberType type = entry.getValue().getType();
                    AttributeDeclaration att = entry.getValue();
                    if(instanceGenericTypes.containsKey(type.getName())){
                        type = instanceGenericTypes.get(type.getName());
                        att = new AttributeDeclaration(entry.getValue().getName(), type);
                        att.setPosition(entry.getValue().getPosition());
                    }
                    if(entry.getValue().isPublic())toAdd.put(entry.getKey(), att);
                    else toAddPrivate.put(entry.getKey(), att);
                    if(!entry.getValue().isStatic()) position++;
                }

            }

            attributes.putAll(toAdd);
            blindAttributes.putAll(toAddPrivate);
            position += toAddPrivate.size();
            for( AttributeDeclaration a: sortedAttributes){
                if(!a.isStatic()) a.setPosition(position++);

            }
            dynamicAtt = position;
            coveredAtt = covered;
            ArrayList<MethodDeclaration> parentMethods = SymbolTable.getMethods(parent.getLexeme());
            System.out.println("Cheking class "+name.getLexeme());
            for(int i = parentMethods.size()-1; i>= 0; i--){
                if(parentMethods.get(i).isPublic()){
                    String key = "#"+parentMethods.get(i).getParametersSize()+"#"+parentMethods.get(i).getName().getLexeme();
                    if(!methods.containsKey(key)){
                        if(parentMethods.get(i).isAbstract() && !isAbstract) throw new SemanticalErrorException(name, "Method "+parentMethods.get(i).getName().getLexeme()+" in class "+name.getLexeme()+" must be implemented!");

                        MethodDeclaration m = parentMethods.get(i);
                        if(instanceGenericTypes.containsKey(parentMethods.get(i).getType().getName())){
                            MemberType type = instanceGenericTypes.get(parentMethods.get(i).getType().getName());
                            m = new MethodDeclaration(parentMethods.get(i).getName(), type);
                            m.copy(parentMethods.get(i));


                        }else methods.put(key, m);
                        ancestorMethods.put(key,m);
                        sortedMethods.addFirst(m);
                        System.out.println("Adding "+m.getName().getLexeme()+" type "+m.getType());
                    } else{
                        if(!methods.get(key).sameSignature(parentMethods.get(i)))
                            throw new SemanticalErrorException(methods.get(key).getName(), "Method "+parentMethods.get(i).getName().getLexeme()+" in class "+name.getLexeme()+" cant redefine method with different signature in Parent class");
                        sortedMethods.remove(methods.get(key));
                        sortedMethods.addFirst(methods.get(key));

                    }
                }
            }


            isConsolidated = true;
        }
        return true;
    }



    public HashMap<String, AttributeDeclaration> getAttributes() {
        return attributes;
    }

    public ArrayList<MethodDeclaration> getMethods() {
        return sortedMethods;
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

    public Token getParent() {
        return parent;
    }

    public void addParametricType(Token genericType) throws SemanticalErrorException {
        MemberObjectType newType = new MemberObjectType(genericType);
        if(!parametricTypes.containsKey(genericType.getLexeme())){
            parametricTypes.put(genericType.getLexeme(), newType);
        }
        orderedParametricTypes.add(newType);
       // else throw new SemanticalErrorException(genericType, "Parametric Type "+genericType.getLexeme()+" already exists in class "+name.getLexeme());
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

    public void addLocalVar(LocalVar localVar) throws SemanticalErrorException{
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

    public ParameterDeclaration visibleParameter(Token name) {
        return currentMethod.visibleParameter(name);
    }
    public AttributeDeclaration visibleAttribute(Token name) {
        return attributes.get(name.getLexeme());
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

    public boolean validStatements() throws CompilerException{
        for (HashMap.Entry<String, ConstructorDeclaration> entry : constructors.entrySet()){
            currentMethod = entry.getValue();
            if(!entry.getValue().validStatements()) return false;
        }
        for( HashMap.Entry<String, MethodDeclaration> entry : methods.entrySet()){
            currentMethod = entry.getValue();
            if(!entry.getValue().validStatements()) return false;
        }
        return true;
    }

    public MethodDeclaration getMethod(AccessMethod accessMethod) {
        return methods.get("#"+accessMethod.getParametersSize()+"#"+accessMethod.getName().getLexeme());
    }

    public AttributeDeclaration getAttribute(AccessVar accessVar) {
        return attributes.get(accessVar.getName().getLexeme());
    }

    public MethodDeclaration getReturnType() {
        return currentMethod;
    }

    public ConstructorDeclaration findConstructor(Token name, int size) {
        String key = "#"+size+"#";
        if(constructors.containsKey(key)) return constructors.get(key);
        else if(size == 0) return constructors.get("default");
        return null;

    }

    public void setInstanceType(MemberObjectType generic, MemberObjectType instance) {
        System.out.println("IN class"+name.getLexeme()+" setting "+generic.getName()+" to "+instance.getName());
        instanceGenericTypes.put(generic.getName(), instance);

    }

    public boolean instanciates(MemberType instance, MemberType generic) {
        System.out.println("Is "+instance.getName()+" instance of "+generic.getName()+"?");
        if(instanceGenericTypes.containsKey(generic.getName())) {
            if (instance.getName().equals(instanceGenericTypes.get(generic.getName()).getName()))
                return true;
        }
        return false;
    }

    public int getAttAmount() {
        return dynamicAtt + coveredAtt;
    }

    public boolean staticContext() {
        return currentMethod.isStatic();
    }

    public void generate() {
        fileWriter.add(".DATA");

        int i = 0;
        int off = 0;
        boolean hasMet = false;
        while(i<sortedMethods.size()){
            if(!sortedMethods.get(i).isAbstract() && !sortedMethods.get(i).isStatic()){
                if(!hasMet){
                    fileWriter.add("lblVT"+name.getLexeme()+": DW "+sortedMethods.get(i).getLabel());


                    hasMet = true;
                }
                else fileWriter.add("DW "+sortedMethods.get(i).getLabel());
                sortedMethods.get(i).setOffset(off);
                off++;
            }
            i++;
        }
        if(!hasMet) fileWriter.add("lblVT"+name.getLexeme()+": NOP" );

        for(HashMap.Entry<String, AttributeDeclaration> entry : attributes.entrySet()){
            if(entry.getValue().isStatic()) {
                String staticLabel = "lblAttr"+entry.getValue().getName().getLexeme()+"@"+name.getLexeme();
                fileWriter.add(staticLabel+": DW 0");
                entry.getValue().setStaticLabel(staticLabel);
            }
        }
        fileWriter.add(".CODE");
        for(HashMap.Entry<String, ConstructorDeclaration> entry : constructors.entrySet()){
            entry.getValue().generate();
        }
        for(HashMap.Entry<String, MethodDeclaration> entry : methods.entrySet()){
            entry.getValue().generate();
        }
    }

    public String getVtLabel() {
        return "lblVT"+name.getLexeme();
    }

    public void addHeritageGenerics(ArrayList<MemberObjectType> parametricTypes) {
        parentGenericInstance = parametricTypes;
    }
}
