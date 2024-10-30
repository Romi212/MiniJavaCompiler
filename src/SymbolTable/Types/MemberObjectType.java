package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import AST.Expressions.Access.AccessMethod;
import AST.Expressions.Access.AccessVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberObjectType extends MemberType{

    private ArrayList<MemberObjectType> attributes;


    private HashMap<String,MemberObjectType> parametricMap;

    public MemberObjectType(Token name){
        this.name = name;
        attributes = new ArrayList<>();
        parametricMap = new HashMap<>();
    }

    public boolean isCorrect() throws SemanticalErrorException {
        HashMap<String,MemberType> validParametrictypes = SymbolTable.getParametricTypes();
        boolean isStatic = SymbolTable.getCurrentMember().isStatic();
        if(SymbolTable.hasClass(this.name)){
            ClassDeclaration classType = SymbolTable.getClass(this.name);
            ArrayList<MemberObjectType> orderedParam = classType.getParametricTypes();
            if(classType.genericParametersAmount() == attributes.size()){
                for (MemberObjectType attribute : attributes) {
                    parametricMap.put(orderedParam.get(attributes.indexOf(attribute)).getName(),attribute);
                    System.out.println(orderedParam.get(attributes.indexOf(attribute)).getName()+"->"+attribute.getName());
                    if (!SymbolTable.hasClass(attribute.getToken())) {
                        if (isStatic)
                            throw new SemanticalErrorException(attribute.getToken(), "Class " + attribute.getToken().getLexeme() + " does not exist AND because is static it cannot have a generic type");
                        if (!validParametrictypes.containsKey(attribute.getName()))
                            throw new SemanticalErrorException(attribute.getToken(), "Class " + attribute.getToken().getLexeme() + " does not exist or is not valid generic type to instantiate the generic types of " + name.getLexeme());
                    }
                }
                return true;
            } else {
                throw new SemanticalErrorException(name, "Class "+name.getLexeme()+" has "+classType.genericParametersAmount()+" generic parameters, but "+attributes.size()+" were given");
            }
        } else{
            if(validParametrictypes.containsKey(name.getLexeme())){
                if(isStatic) throw new SemanticalErrorException(name, "Member is static so it cannot be declared of a generic type.");
                if (attributes.size()>0) throw new SemanticalErrorException(name, "Generic type "+name.getLexeme()+" cannot have generic parameters");
                return true;
            } else throw new SemanticalErrorException(name, "Class "+name.getLexeme()+" does not exist nor is a valid parametric type");
        }



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

    public MemberDeclaration hasMember(AccessMember link) {
        return SymbolTable.getClass(this.name).getMember(link);
    }

    public MethodDeclaration hasMethod(AccessMethod accessMethod) {
        return SymbolTable.getClass(this.name).getMethod(accessMethod);
    }

    public AttributeDeclaration hasAttribute(AccessVar accessVar) {
        return SymbolTable.getClass(this.name).getAttribute(accessVar);
    }

    public boolean conformsTo(MemberType type) {
        if(type == null) return false;
        if(name.getLexeme().equals(type.getName())) {
            if(attributes.size()>0){
                MemberObjectType obj = (MemberObjectType) type;
                ArrayList<MemberObjectType> params = obj.getAttributes();
                if(params.size() != attributes.size()) return false;
                for(int i = 0; i<attributes.size(); i++){
                    if(!attributes.get(i).conformsTo(params.get(i))) return false;
                }
            }
                return true;
        }
        else{
            return SymbolTable.isAncestor(type.getName(), name.getLexeme());
        }
    }

    public boolean conformsTo(String type) {

        return name.getLexeme().equals(type) || SymbolTable.isAncestor(type, name.getLexeme());
    }



    public MemberType transformType(MemberType type){
        if (parametricMap.containsKey(type.getName()))
            return parametricMap.get(type.getName());
        return type;
    }


    public String toString(){
        String toReturn = name.getLexeme();

        return toReturn;
    }
}
