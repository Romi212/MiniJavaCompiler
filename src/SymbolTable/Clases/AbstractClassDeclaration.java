package SymbolTable.Clases;

import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.HashMap;

public class AbstractClassDeclaration extends ClassDeclaration{

    protected HashMap<String, MethodDeclaration> abstractMethods;

    public AbstractClassDeclaration(ClassDeclaration classDeclaration){
        super(classDeclaration.getName());
        setParent(classDeclaration.getParent());
        constructor = null;
        isAbstract = true;
        abstractMethods = new HashMap<>();
    }



    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public MethodDeclaration addAbstractMethod(Token name, Token returnType) throws SemanticalErrorException  {
        MethodDeclaration newMethod = new MethodDeclaration(name, SymbolTable.decideType(returnType));
        newMethod.setAbstract(true);
        methods.put(name.getLexeme(), newMethod);
        return newMethod;
    }

    public String toString(){

            String p = "";
            if(parent != null) p = this.parent.getLexeme();
            else p = "Nill";
            return "Class: "+this.name.getLexeme()+"\n extends: "+p+"\n Attributes: "+this.attributes.toString()+"\n Methods: "+this.methods.toString()+"\n" + "Abstract Methods: "+this.abstractMethods.toString()+"\n";

    }

}
