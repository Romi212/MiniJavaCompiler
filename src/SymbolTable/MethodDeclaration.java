package SymbolTable;

import AST.BLockDeclaration;
import AST.LocalVar;
import AST.Statements.StatementNode;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.HashMap;

public class MethodDeclaration extends MemberDeclaration {

    protected Token name;
    protected MemberType returnType;
    protected boolean isAbstract = false;
    protected HashMap<String, ParameterDeclaration> parameters = new HashMap<>();

    protected BLockDeclaration block;


    public MethodDeclaration(Token name, MemberType returnType){
        this.name = name;
        this.returnType = returnType;
        this.isStatic = false;
        this.visibility = new Token("pw_public", "public", -1);
        block = new BLockDeclaration();
    }

    public void addBlock(BLockDeclaration block){
        this.block = block;
    }
    public Token getName(){
        return this.name;
    }

    public boolean hasLocalVar(String name){
        return block.containsLocalVar(name);
    }
    public void addStatement(StatementNode statement){
        block.addStatement(statement);
    }
    public String toString(){

        String staticT = isStatic ? "static " : "";
        return "["+visibility.getLexeme()+" "+ staticT+" " + name.getLexeme() + "("+ parameters +") : " + returnType.getName()+"] \n"+block.toString()+"\n";
    }

    public void addParameter(Token argName, MemberType argType) throws SemanticalErrorException {

        if(parameters.containsKey(argName.getLexeme())) throw new SemanticalErrorException(argName, "Parameter "+argName.getLexeme()+" in method "+name.getLexeme()+" already exists");
        int pos = parameters.size();
        parameters.put(argName.getLexeme(), new ParameterDeclaration(argName,argType,pos));

    }

    public boolean isCorrectlyDeclared() throws CompilerException {

        if(returnType.isCorrect()){
            for (HashMap.Entry<String, ParameterDeclaration> entry : parameters.entrySet()) {
                if(!entry.getValue().isCorrectlyDeclared()){
                    if(isStatic ) {
                        throw new SemanticalErrorException(entry.getValue().getType().getToken(), "Parameter "+entry.getValue().getName().getLexeme()+" in method "+name.getLexeme()+" has an invalid type "+entry.getValue().getType().getName());
                    }
                }
            }
            if(isAbstract && !visibility.getLexeme().equals("public")) throw new SemanticalErrorException(visibility, "Abstract method "+name.getLexeme()+" must be public");

        } else throw new SemanticalErrorException(returnType.getToken(), "Method "+name.getLexeme()+"  has an invalid return type "+returnType.getName());

        return block.isCorrect();

    }

    public boolean sameSignature(MethodDeclaration otherMethod) {
        if(!this.name.getLexeme().equals(otherMethod.getName().getLexeme())) return false;

        HashMap<String, ParameterDeclaration> otherParameters = otherMethod.getParameters();

        if(this.parameters.size() != otherParameters.size()) return false;

        for (HashMap.Entry<String, ParameterDeclaration> entry : this.parameters.entrySet()) {
            String key = entry.getKey();
            if(!otherParameters.containsKey(key)) return false;
            if(!entry.getValue().sameType(otherParameters.get(key))) return false;
        }

        if(!this.returnType.getName().equals(otherMethod.getReturnType().getName())) return false;

        if(this.isStatic != otherMethod.isStatic) return false;

        if(!this.visibility.getLexeme().equals(otherMethod.visibility.getLexeme())) return false;

        return true;
    }

    public MemberType getReturnType() {
        return this.returnType;
    }

    private HashMap<String, ParameterDeclaration> getParameters() {
        return this.parameters;
    }

    public int getParametersSize(){
        return this.parameters.size();
    }

    public void setAbstract(boolean b) {
        this.isAbstract = b;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }


    public void addLocalVar(LocalVar localVar) {
        block.addLocalVar(localVar);
    }

    public boolean hasParameter(String lexeme) {
        return parameters.containsKey(lexeme);
    }
}
