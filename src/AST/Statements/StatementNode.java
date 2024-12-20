package AST.Statements;

import AST.LocalVar;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.HashMap;

abstract public class StatementNode {

    public Token name;
    public StatementNode parent;
    public boolean staticContext = false;

    private String breakLabel;

    private String label;

    private HashMap<String, LocalVar> localVars;
    public StatementNode(Token name){
        this.name = name;
        this.localVars = new HashMap<>();
        this.label = "end@"  + SymbolTable.getIndex();
    }
    public void setStaticContext(boolean isStatic){
        this.staticContext = isStatic;
    }

    public boolean isStaticContext(){
        return (parent == null && staticContext || parent.isStaticContext());
    }

    public void setName(Token name){
        this.name = name;
    }
    public void addLocalVar(LocalVar localVar) throws SemanticalErrorException {

        if(localVars.containsKey(localVar.getName().getLexeme())){
            throw new SemanticalErrorException(localVar.getName(),"Variable " + localVar.getName().getLexeme() + " already declared in the same block");
        }
        if(parent != null && parent.containsLocalVar(localVar.getName().getLexeme())){
            throw new SemanticalErrorException(localVar.getName(),"Variable " + localVar.getName().getLexeme() + " already declared in ancestor block");
        }
        this.localVars.put(localVar.getName().getLexeme(), localVar);



    }

    public LocalVar getLocalVar(String name){

        if(this.localVars.containsKey(name)) return this.localVars.get(name);
        if(parent != null && parent!= this) return parent.getLocalVar(name);
        return null;
    }

    public void setParent(StatementNode parent){
        this.parent = parent;
    }

    public boolean containsLocalVar(String name){

        return this.localVars.containsKey(name) || (parent != null && parent.containsLocalVar(name));
    }

    public abstract boolean isCorrect() throws CompilerException;


    public Token getName() {
        return name;
    }

    public MemberType visibleVar(Token name) {
        if(localVars.containsKey(name.getLexeme())){
            return localVars.get(name.getLexeme()).getType();
        }
        if(parent!= null) return parent.visibleVar(name);
        return null;
    }

    public boolean isBreakable() {
        return (parent != null && parent.isBreakable());
    }

    public void generate() {
    }

    public int getLocalVarSize(){
        return localVars.size();
    }

    public String getBreakLabel(){
        return breakLabel;
    }

    public void setBreakLabel(String label){
        this.breakLabel = label;
    }

    public String getEndLabel(){

        return label;
    }

    protected void freeVars() {
        //SymbolTable.removeLocalVar(getLocalVarSize());
        if(parent == null) return;
        fileWriter.add("FMEM " + getLocalVarSize());

        parent.freeVars();
    }
}
