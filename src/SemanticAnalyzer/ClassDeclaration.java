package SemanticAnalyzer;

import SemanticAnalyzer.Attributes.*;

import java.util.HashMap;

public class ClassDeclaration {

    private String name;
    private String parent;
    private boolean isAbstract;

    private boolean isConsolidated;
    private HashMap<String, AttributeDeclaration> attributes;
    private HashMap<String,MethodDeclaration> methods;

    public ClassDeclaration(String name){
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

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    public void addAttribute(String attribute, String type){
        AttributeDeclaration newAtr = null;
        switch (type){
            case "int":{
                newAtr = new IntAttribute(attribute);
                break;
            }
            case "char":{
                newAtr = new CharAttribute(attribute);
                break;
            }
            case "boolean":{
                newAtr = new BoolAttribute(attribute);
                break;
            }
            case "String":{
                newAtr = new StringAttribute(attribute);
                break;
            }

            default:
                newAtr = new ObjectAttribute(attribute, type);
                break;
        }
        this.attributes.put(attribute, newAtr);
    }

    public void addMethod(String method, String returnType){
        this.methods.put(method, new MethodDeclaration(method, returnType));
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Class: "+this.name+"\n extends: "+this.parent+"\n Attributes: "+this.attributes.toString()+"\n Methods: "+this.methods.toString()+"\n";
    }

    public void makeAbstract() {
        this.isAbstract = true;
    }
}
