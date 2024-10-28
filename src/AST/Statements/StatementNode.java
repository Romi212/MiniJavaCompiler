package AST.Statements;

import AST.LocalVar;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.HashMap;

abstract public class StatementNode {

    public Token name;
    public StatementNode parent;
    public boolean staticContext = false;

    private HashMap<String, LocalVar> localVars;
    public StatementNode(Token name){
        this.name = name;
        this.localVars = new HashMap<>();
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
        //TODO CHECK REPETIDO
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
        if(parent != null) return parent.getLocalVar(name);
        return null;
    }

    public void setParent(StatementNode parent){
        if(parent == null) System.out.println(this+"Setting parent to null");
        this.parent = parent;
    }

    public boolean containsLocalVar(String name){
        System.out.println("Checking if contains "+name + " in "+this);

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
}
