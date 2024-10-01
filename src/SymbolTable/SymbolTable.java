package SymbolTable;

import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Types.*;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.HashMap;


public class SymbolTable {
    static private HashMap<String,ClassDeclaration> symbolTable = new HashMap<>();
    static private ClassDeclaration currentClass;

    private static boolean hasMain= false;
    private static MethodDeclaration mainMethod;

    public static void createTable(){
        symbolTable = new HashMap<>();


        try {
            ClassDeclaration object = new ClassDeclaration(new Token("pc_object", "Object", -1));
            MethodDeclaration debugPrint =  object.addMethod(new Token("pc_object", "debugPrint", -1), new Token("pc_object", "void", -1));
            debugPrint.addParameter(new Token("pc_object", "i", -1), new Token("pc_object", "int", -1));
            object.setConsolidated(true);

            symbolTable.put("Object", object);

            ClassDeclaration string = new ClassDeclaration(new Token("pc_object", "String", -1));
            string.setConsolidated(true);

            symbolTable.put("String", string);
            ClassDeclaration system = new ClassDeclaration(new Token("pc_object", "System", -1));
            system.setConsolidated(true);
            system.addMethod(new Token("pc_object", "read", -1), new Token("pc_object", "int", -1));
            MethodDeclaration printB = system.addMethod(new Token("pc_object", "printB", -1), new Token("pc_object", "void", -1));
            printB.addParameter(new Token("pc_object", "b", -1), new Token("pc_object", "boolean", -1));
            MethodDeclaration printC = system.addMethod(new Token("pc_object", "printC", -1), new Token("pc_object", "void", -1));
            printC.addParameter(new Token("pc_object", "c", -1), new Token("pc_object", "char", -1));
            MethodDeclaration printI = system.addMethod(new Token("pc_object", "printI", -1), new Token("pc_object", "void", -1));
            printI.addParameter(new Token("pc_object", "i", -1), new Token("pc_object", "int", -1));
            MethodDeclaration printS = system.addMethod(new Token("pc_object", "printS", -1), new Token("pc_object", "void", -1));
            printS.addParameter(new Token("pc_object", "s", -1), new Token("pc_object", "String", -1));
            system.addMethod(new Token("pc_object", "println", -1), new Token("pc_object", "void", -1));
            MethodDeclaration printBln = system.addMethod(new Token("pc_object", "printBln", -1), new Token("pc_object", "void", -1));
            printBln.addParameter(new Token("pc_object", "b", -1), new Token("pc_object", "boolean", -1));
            MethodDeclaration printCln = system.addMethod(new Token("pc_object", "printCln", -1), new Token("pc_object", "void", -1));
            printCln.addParameter(new Token("pc_object", "c", -1), new Token("pc_object", "char", -1));
            MethodDeclaration printIln = system.addMethod(new Token("pc_object", "printIln", -1), new Token("pc_object", "void", -1));
            printIln.addParameter(new Token("pc_object", "i", -1), new Token("pc_object", "int", -1));
            MethodDeclaration printSln = system.addMethod(new Token("pc_object", "printSln", -1), new Token("pc_object", "void", -1));
            printSln.addParameter(new Token("pc_object", "s", -1), new Token("pc_object", "String", -1));
            symbolTable.put("System", system);
        } catch (SemanticalErrorException e) {
            e.printStackTrace();
        }



    }

    public static void addClass(Token className) throws SemanticalErrorException{
        if(!symbolTable.containsKey(className.getLexeme())){
        symbolTable.put(className.getLexeme(), new ClassDeclaration(className));
        currentClass = symbolTable.get(className);
        }
        else{
            throw new SemanticalErrorException(className, "Class "+className.getLexeme()+" already exists");
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

    public static AttributeDeclaration addAttribute(Token attribute, Token type) throws SemanticalErrorException{

        return currentClass.addAttribute(attribute, type);
    }

    public static MethodDeclaration addMethod(Token method, Token returnType) throws SemanticalErrorException{

        MethodDeclaration methodDeclaration = currentClass.addMethod(method, returnType);
        if(method.getLexeme().equals("main") && returnType.getLexeme().equals("void")){

            if(!hasMain || mainMethod.getParametersSize() > 0) {
                mainMethod = methodDeclaration;
                hasMain = true;
            }
        }
        return methodDeclaration;
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

    public static MethodDeclaration addConstructor(Token name) throws   SemanticalErrorException {
        return currentClass.addConstructor(name);
    }

    public static void isConsolidated(Token parent) throws SemanticalErrorException{
        if(symbolTable.containsKey(parent.getLexeme())){
            ClassDeclaration parentClass = symbolTable.get(parent.getLexeme());
            parentClass.isConsolidated();

        }else  throw new SemanticalErrorException(parent, "Parent class "+parent.getLexeme()+" is not declared");
    }

    public static HashMap<String, AttributeDeclaration> getAttributes(String padre) {
        return symbolTable.get(padre).getAttributes();
    }

    public static HashMap<String,MethodDeclaration> getMethods(String padre) {
        return symbolTable.get(padre).getMethods();
    }

    public static boolean hasClass(Token name) throws SemanticalErrorException {
        if (symbolTable.containsKey(name.getLexeme())) return true;
        else throw new SemanticalErrorException(name, "Attribute declared of class "+name.getLexeme()+" that doesn't exist");
    }

    public static boolean isCorrect()throws CompilerException {

        if (!hasMain || mainMethod.getParametersSize()>0) throw new SemanticalErrorException(new Token("pc_object", "main", -1), "Main method declared correctly not found");

        for (HashMap.Entry<String, ClassDeclaration> entry : symbolTable.entrySet()) {
            ClassDeclaration classDeclaration = entry.getValue();
            if(!classDeclaration.isConsolidated()) return false;
        }

        return true;
    }
}
