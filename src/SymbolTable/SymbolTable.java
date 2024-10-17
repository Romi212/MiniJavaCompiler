package SymbolTable;

import AST.LocalVar;
import AST.Statements.StatementNode;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.Types.*;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.HashMap;


public class SymbolTable {
    static private HashMap<String, ClassDeclaration> symbolTable = new HashMap<>();
    static private ClassDeclaration currentClass;

    private static boolean hasMain;
    private static MethodDeclaration mainMethod;

    public static void createTable(){
        symbolTable = new HashMap<>();
        hasMain = false;

        try {
            ClassDeclaration object = new ClassDeclaration(new Token("pc_object", "Object", -1));
            MethodDeclaration debugPrint =  object.addMethod(new Token("pc_object", "debugPrint", -1), new VoidType(new Token("pc_object", "void", -1)));
            debugPrint.addParameter(new Token("pc_object", "i", -1), new IntegerType(new Token("pc_object", "int", -1)));
            object.setConsolidated(true);

            symbolTable.put("Object", object);

            ClassDeclaration string = new ClassDeclaration(new Token("pc_object", "String", -1));
            string.setConsolidated(true);

            symbolTable.put("String", string);
            ClassDeclaration system = new ClassDeclaration(new Token("pc_object", "System", -1));
            system.setConsolidated(true);
            system.addMethod(new Token("pc_object", "read", -1), new IntegerType(new Token("pc_object", "int", -1)));
            MethodDeclaration printB = system.addMethod(new Token("pc_object", "printB", -1), new VoidType(new Token("pc_object", "void", -1)));
            printB.addParameter(new Token("pc_object", "b", -1), new BooleanType(new Token("pc_object", "boolean", -1)));
            MethodDeclaration printC = system.addMethod(new Token("pc_object", "printC", -1), new VoidType(new Token("pc_object", "void", -1)));
            printC.addParameter(new Token("pc_object", "c", -1), new CharacterType(new Token("pc_object", "char", -1)));
            MethodDeclaration printI = system.addMethod(new Token("pc_object", "printI", -1), new VoidType(new Token("pc_object", "void", -1)));
            printI.addParameter(new Token("pc_object", "i", -1), new IntegerType(new Token("pc_object", "int", -1)));
            MethodDeclaration printS = system.addMethod(new Token("pc_object", "printS", -1), new VoidType(new Token("pc_object", "void", -1)));
            printS.addParameter(new Token("pc_object", "s", -1), new MemberObjectType(new Token("pc_object", "String", -1)));
            system.addMethod(new Token("pc_object", "println", -1), new VoidType(new Token("pc_object", "void", -1)));
            MethodDeclaration printBln = system.addMethod(new Token("pc_object", "printBln", -1), new VoidType(new Token("pc_object", "void", -1)));
            printBln.addParameter(new Token("pc_object", "b", -1), new BooleanType(new Token("pc_object", "boolean", -1)));
            MethodDeclaration printCln = system.addMethod(new Token("pc_object", "printCln", -1), new VoidType(new Token("pc_object", "void", -1)));
            printCln.addParameter(new Token("pc_object", "c", -1), new CharacterType(new Token("pc_object", "char", -1)));
            MethodDeclaration printIln = system.addMethod(new Token("pc_object", "printIln", -1), new VoidType(new Token("pc_object", "void", -1)));
            printIln.addParameter(new Token("pc_object", "i", -1), new IntegerType(new Token("pc_object", "int", -1)));
            MethodDeclaration printSln = system.addMethod(new Token("pc_object", "printSln", -1), new VoidType(new Token("pc_object", "void", -1)));
            printSln.addParameter(new Token("pc_object", "s", -1), new MemberObjectType(new Token("pc_object", "String", -1)));
            symbolTable.put("System", system);
        } catch (SemanticalErrorException e) {
            e.printStackTrace();
        }



    }


    public static void addClass(ClassDeclaration classDeclaration) throws SemanticalErrorException{
        if(!symbolTable.containsKey(classDeclaration.getName().getLexeme())){
            symbolTable.put(classDeclaration.getName().getLexeme(), classDeclaration);
            currentClass = classDeclaration;
        }
        else{
            throw new SemanticalErrorException(classDeclaration.getName(), "Class "+classDeclaration.getName().getLexeme()+" already exists");
        }

    }

    public static AttributeDeclaration addAttribute(Token attribute, MemberType type) throws SemanticalErrorException{

        return currentClass.addAttribute(attribute, type);
    }

    public static MethodDeclaration addMethod(MethodDeclaration method) throws SemanticalErrorException{

        currentClass.addMethod(method);
        if(method.getName().getLexeme().equals("main") && method.getReturnType().getName().equals("void") && method.getParametersSize() == 0){

            if(!hasMain ) {
                mainMethod = method;
                hasMain = true;
            }
        }
        return method;
    }

    public static String showString(){
        String print = "";
            for (HashMap.Entry<String, ClassDeclaration> entry : symbolTable.entrySet()) {

                ClassDeclaration classDeclaration = entry.getValue();
                print += classDeclaration.toString() + "\n";
            }
        return print;
    }

    public static MemberType decideType(Token type){
        MemberType memberType ;
        switch (type.getLexeme()){
            case "int":{
                memberType = new IntegerType(type);
                break;
            }
            case "char":{
                memberType = new CharacterType(type);
                break;
            }
            case "boolean":{
                memberType = new BooleanType(type);
                break;
            }
            case "String":{
                memberType = new MemberObjectType(type);
                break;
            }
            case "void":{
                memberType = new VoidType(type);
                break;
            }

            default:
                memberType = new MemberObjectType(type);
                break;
        }
        return memberType;
    }

    public static MethodDeclaration addConstructor(ConstructorDeclaration constructor) throws   SemanticalErrorException {
        return currentClass.addConstructor(constructor);
    }

    public static void isConsolidated(Token parent) throws CompilerException{
        if(symbolTable.containsKey(parent.getLexeme())){
            ClassDeclaration parentClass = symbolTable.get(parent.getLexeme());
            ClassDeclaration child = currentClass;
            currentClass = parentClass;
            parentClass.isConsolidated();
            currentClass = child;

        }else  throw new SemanticalErrorException(parent, "Parent class "+parent.getLexeme()+" is not declared");
    }

    public static HashMap<String,MemberType> getParametricTypes() {
        return currentClass.getParametricTypesSet();
    }
    public static HashMap<String, AttributeDeclaration> getAttributes(String padre) {
        return symbolTable.get(padre).getAttributes();
    }

    public static HashMap<String,MethodDeclaration> getMethods(String padre) {
        return symbolTable.get(padre).getMethods();
    }

    public static boolean hasClass(Token name) throws SemanticalErrorException {
        if (symbolTable.containsKey(name.getLexeme())) return true;
        else return  false;
    }

    public static boolean isCorrect(Token EOF)throws CompilerException {

        if (!hasMain || mainMethod.getParametersSize()>0) throw new SemanticalErrorException(EOF, "Main method declared correctly not found");


        for (HashMap.Entry<String, ClassDeclaration> entry : symbolTable.entrySet()) {
            ClassDeclaration classDeclaration = entry.getValue();
            currentClass = classDeclaration;
            if(!classDeclaration.isConsolidated()) return false;
        }

        return true;
    }

    public static void addAbstractMethod(MethodDeclaration method) throws SemanticalErrorException {
        currentClass.addAbstractMethod(method);
    }

    public static boolean isAbstract(Token parent) {
        return symbolTable.get(parent.getLexeme()).isAbstract();
    }

    public static void checkParent(ClassDeclaration parent) throws SemanticalErrorException {
        if(symbolTable.containsKey(parent.getName().getLexeme())){
            int expectedParameters = symbolTable.get(parent.getName().getLexeme()).genericParametersAmount();
            if(expectedParameters != parent.genericParametersAmount())
                throw new SemanticalErrorException(parent.getName(), "Parent class "+parent.getName().getLexeme()+" declared with "+parent.genericParametersAmount()+" generic parameters, expected "+expectedParameters);

        }
    }

    public static ClassDeclaration getClass(Token name) {
        return symbolTable.get(name.getLexeme());
    }

    public static MemberDeclaration getCurrentMember() {
        return currentClass.getCurrentMember();
    }

    public static void addStatement(StatementNode statement) {
        currentClass.addStatement(statement);
    }

    public static void addLocalVar(LocalVar localVar) {
        currentClass.addLocalVar(localVar);
    }

    public static boolean isLocalVar(Token name) {
        return currentClass.isLocalVar(name);
    }

    public static boolean isParameter(Token name) {
        return currentClass.isParameter(name);
    }

    public static boolean isAtribute(Token name) {
        return currentClass.isAtribute(name);
    }
}
