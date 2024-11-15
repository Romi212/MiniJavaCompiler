package AST.Expressions.Access;

import AST.LocalVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class AccessVar extends AccessMember{


    AttributeDeclaration attribute;

    private LocalVar localvar;

    private ParameterDeclaration parameter;

    boolean isStatic = true;



    public AccessVar() {
        super(null);
    }

    public boolean isCorrect() throws SemanticalErrorException{
        LocalVar var = parent.getLocalVar(this.name.getLexeme());
        if(var != null){
            this.localvar = var;
            this.type = var.getType();

            return true;
        }
        ParameterDeclaration par = SymbolTable.visibleParameter(this.name);
        if(par == null){
            AttributeDeclaration a = SymbolTable.visibleAttribute(this.name);
            this.attribute = a;
            if(a!= null) {
                type = a.getType();
                isStatic = a.isStatic();
            }
        }else {
            this.parameter = par;
            this.type = par.getType();
        }

        if(type == null) throw new SemanticalErrorException(this.name,"Variable "+this.name.getLexeme()+" not found in current context");

        return true;
    }

    public MemberType getExpressionType(){
        return this.type;
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        MemberType parentType = parent.getExpressionType();
        AttributeDeclaration a = parentType.hasAttribute(this);
        if(a == null) throw new SemanticalErrorException( this.name,"Attribute "+this.name.getLexeme()+" not found in "+parent.getExpressionType().getName());
        this.attribute = a;

        if (!a.isPublic()) throw new SemanticalErrorException(this.name, "Attribute cant be accessed because "+this.name.getLexeme()+" is not public");
        isStatic = a.isStatic();
        this.type = parentType.transformType(a.getType());
    }

    public String toString(){
        if (this.attribute == null) return this.name.getLexeme();
        return attribute.toString();
    }

    public boolean isStatement(){
        return false;
    }

    public boolean isAssignable(){
        return true;
    }

    public boolean isStatic(){
        return isStatic;
    }


    public void generate() {
        if(localvar != null) {
            if (write) {
                fileWriter.add("STORE "+localvar.getOffset()+" ; guarda el rtado en la variable");
            }
            else fileWriter.add("LOAD "+localvar.getOffset()+" ; carga el valor de la variable");
        }
        else{
            if(attribute != null){
                if(write){
                  fileWriter.add("LOAD 3 ; carga ref a this");
                  fileWriter.add("SWAP");
                  fileWriter.add("STOREREF "+(attribute.getPosition()+1)+" ; carga el valor del atributo");
                }else{
                    fileWriter.add("LOAD 3 ; carga ref a this");
                    fileWriter.add("LOADREF "+(attribute.getPosition()+1)+" ; carga el valor del atributo");
                }
            }else{
                if(parameter!= null){
                    if(write){
                        fileWriter.add("STOREFP "+(parameter.getPosition()+4)+" ; guarda el rtado en el parametro");
                    }else{
                        fileWriter.add("LOADFP "+(parameter.getPosition()+4)+" ; carga el valor del parametro");
                    }
                }
            }
        }
    }
}
