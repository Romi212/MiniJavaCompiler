package AST.Expressions.Access;

import AST.LocalVar;
import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.ConstructorDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class AccessNewObject extends AccessMember{

    private ConstructorDeclaration constructor;
    ClassDeclaration classD;
    public AccessNewObject(Token name, MemberObjectType type){
        super(name);
        this.type = type;
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if( !SymbolTable.hasClass(type.getToken())) throw new SemanticalErrorException(getName(), "Class "+type.getToken().getLexeme()+" does not exist");
        classD = SymbolTable.getClass(type.getToken());
        if(classD.isAbstract()) throw new SemanticalErrorException(getName(), "Class "+type.getToken().getLexeme()+" is abstract and cannot be instantiated");
        ConstructorDeclaration constructor = SymbolTable.findConstructor(type.getToken(),parameters.size());
        if(constructor == null) throw new SemanticalErrorException(getName(), "Constructor "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in class "+type.getToken().getLexeme());
        this.constructor = constructor;
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember hasMember) throws SemanticalErrorException{

    }
    public boolean isStatic(){
        return true;
    }

    public boolean isNewObject(){
        return true;
    }
    public void generate(){
        /*RMEM 1  ; Reservamos memoria para el resultado del malloc (la referencia al nuevo CIR de A)
PUSH 2  ;  Apilo la cantidad de var de instancia del CIR de A +1 por VT
PUSH simple_malloc  ; La dirección de la rutina para alojar memoria en el heap
CALL  ; Llamo a malloc
DUP  ; Para no perder la referencia al nuevo CIR
PUSH lblVTA  ; Apilamos la dirección del comienzo de la VT de la clase A
STOREREF 0  ; Guardamos la Referencia a la VT en el CIR que creamos
DUP
PUSH lblConstructor@A
CALL  ; Llamo al constructor lblConstructor@A*/
        fileWriter.add("RMEM 1  ; Reservamos memoria para el resultado del malloc (la referencia al nuevo CIR de A)");
        fileWriter.add("PUSH "+(classD.getAttributes().size()+1)+"  ;  Apilo la cantidad de var de instancia del CIR de A +1 por VT");
        fileWriter.add("PUSH simple_malloc  ; La dirección de la rutina para alojar memoria en el heap");
        fileWriter.add("CALL  ; Llamo a malloc");
        fileWriter.add("DUP  ; Para no perder la referencia al nuevo CIR");
        fileWriter.add("PUSH "+classD.getVtLabel()+"  ; Apilamos la dirección del comienzo de la VT de la clase A");
        fileWriter.add("STOREREF 0  ; Guardamos la Referencia a la VT en el CIR que creamos");
        fileWriter.add("DUP");
        fileWriter.add("PUSH "+constructor.getLabel()+"");
        fileWriter.add("CALL  ; Llamo al constructor "+constructor.getLabel());


    }
}
