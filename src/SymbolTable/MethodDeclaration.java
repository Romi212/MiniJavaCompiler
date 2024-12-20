package SymbolTable;

import AST.BLockDeclaration;
import AST.LocalVar;
import AST.Statements.StatementNode;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodDeclaration extends MemberDeclaration {

    protected Token name;
    protected MemberType returnType;
    protected boolean isAbstract = false;
    protected HashMap<String, ParameterDeclaration> parameters = new HashMap<>();

    protected BLockDeclaration block;
    protected String label;

    protected int offset;

    protected  boolean isGenerated = false;


    public MethodDeclaration(Token name, MemberType returnType){
        this.name = name;
        this.returnType = returnType;
        this.isStatic = false;
        this.visibility = new Token("pw_public", "public", -1);
        block = new BLockDeclaration(new Token("block", "block", -1));
    }
    public void setLabel(String label){
        this.label = label;
    }

    public void addBlock(BLockDeclaration block){
        this.block = block;
        block.setStaticContext(isStatic);
        block.setParent(null);
    }
    public void setOffset(int offset){
        this.offset = offset;
    }

    public int getOffset(){
        return this.offset;
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
                int frame = !isStatic ? 0 : 1;
                entry.getValue().setPosition(parameters.size() - entry.getValue().getPosition()-frame);
            }
            if(isAbstract && !visibility.getLexeme().equals("public")) throw new SemanticalErrorException(visibility, "Abstract method "+name.getLexeme()+" must be public");

        } else throw new SemanticalErrorException(returnType.getToken(), "Method "+name.getLexeme()+"  has an invalid return type "+returnType.getName());

        return true;

    }

    public boolean sameSignature(MethodDeclaration otherMethod) throws SemanticalErrorException {
        if(!this.name.getLexeme().equals(otherMethod.getName().getLexeme())) return false;

        HashMap<String, ParameterDeclaration> otherParameters = otherMethod.getParameters();

        if(this.parameters.size() != otherParameters.size()) return false;

        for (HashMap.Entry<String, ParameterDeclaration> entry : this.parameters.entrySet()) {
            String key = entry.getKey();
            if(!otherParameters.containsKey(key)) return false;
            if(!entry.getValue().sameType(otherParameters.get(key))){
                if(!SymbolTable.instanciates(entry.getValue().getType(), otherParameters.get(key).getType())) return false;
            }
        }
        if(this.returnType == null || otherMethod.getType() == null) {

            return false;
        }
        if(!this.returnType.getName().equals(otherMethod.getType().getName())) {
            if(!SymbolTable.instanciates(this.returnType, otherMethod.getType())) return false;
        }

        ArrayList<MemberObjectType> thisParametricTypes = this.returnType.getAttributes();
        ArrayList<MemberObjectType> otherParametricTypes = otherMethod.getType().getAttributes();
        for(int i = 0; i < otherParametricTypes.size(); i++){
            if(!SymbolTable.instanciates(thisParametricTypes.get(i), otherParametricTypes.get(i))){
                if(!((SymbolTable.hasClass(thisParametricTypes.get(i).getToken()) && thisParametricTypes.get(i).equals(otherParametricTypes.get(i))))){
                    return false;
                }
            }
        }

        if(this.isStatic != otherMethod.isStatic) return false;

        if(!this.visibility.getLexeme().equals(otherMethod.visibility.getLexeme())) return false;

        return true;
    }

    public MemberType getType() {
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


    public void addLocalVar(LocalVar localVar) throws SemanticalErrorException {
        block.addLocalVar(localVar);
    }

    public boolean hasParameter(String lexeme) {
        return parameters.containsKey(lexeme);
    }

    public ParameterDeclaration visibleParameter(Token name) {


        ParameterDeclaration met = parameters.get(name.getLexeme());
        if (met != null) {
            return met;
        }
        return null;
    }

    public boolean validStatements() throws CompilerException {
        if(SymbolTable.stage<4) return true;
        return block.isCorrect();
    }

    public MemberType getParameterType(int indexOf) throws SemanticalErrorException {
        for( ParameterDeclaration p : parameters.values()){
            if(p.getPosition() == indexOf) return p.getType();
        }
        throw new SemanticalErrorException(name, "Method "+name.getLexeme()+" has no parameter at position "+indexOf);
    }

    public void generate() {
        if(isGenerated) return;
        isGenerated = true;
        fileWriter.add(label+": LOADFP ;  comienza ejecucion de metodo "+name.getLexeme());
        fileWriter.add("LOADSP \n STOREFP ; guardamos el ED y acomodamos FP");
        block.generate();
        fileWriter.add("STOREFP ; restauramos el ED y FP");
        int ret = parameters.size();
        if( !isStatic) ret++;
        fileWriter.add("RET "+ret+" ; fin de ejecucion de metodo "+name.getLexeme());
    }

    public String getLabel() {
        return label;
    }

    public String getEndLabel() {
        return  block.getEndLabel();
    }

    public void copy(MethodDeclaration methodDeclaration, HashMap<String, MemberObjectType> instances) {
        this.label = methodDeclaration.label;
        this.offset = methodDeclaration.offset;
        this.isGenerated = true;
        if(instances.containsKey(methodDeclaration.getType().getName())) this.returnType = instances.get(methodDeclaration.getType().getName());
        else this.returnType = methodDeclaration.getType();
        this.isStatic = methodDeclaration.isStatic();
        this.visibility = methodDeclaration.getVisibility();
        this.isAbstract = methodDeclaration.isAbstract();
        for(HashMap.Entry<String, ParameterDeclaration> entry : methodDeclaration.getParameters().entrySet()){
            ParameterDeclaration parameterDeclaration = entry.getValue();
            if(instances.containsKey(parameterDeclaration.getType().getName())){
                parameterDeclaration = new ParameterDeclaration(entry.getValue().getName(), instances.get(entry.getValue().getType().getName()), entry.getValue().getPosition());
            }
            this.parameters.put(entry.getKey(), parameterDeclaration);
        }

        this.block = methodDeclaration.block;



    }
}
