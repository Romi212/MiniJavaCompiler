package SymbolTable;

import SymbolTable.Types.*;
import utils.Token;

import java.util.HashMap;


public class SymbolTable {
    static private HashMap<String,ClassDeclaration> symbolTable = new HashMap<>();
    static private ClassDeclaration currentClass;

    public static void createTable(){
        symbolTable = new HashMap<>();

        ClassDeclaration object = new ClassDeclaration(new Token("pc_object", "Object", -1));
        MethodDeclaration debugPrint =  object.addMethod(new Token("pc_object", "debugPrint", -1), new Token("pc_object", "void", -1));
        debugPrint.addParameter(new Token("pc_object", "i", -1), new Token("pc_object", "int", -1));

        symbolTable.put("Object", object);

        ClassDeclaration string = new ClassDeclaration(new Token("pc_object", "String", -1));

        symbolTable.put("String", string);

        ClassDeclaration system = new ClassDeclaration(new Token("pc_object", "System", -1));
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


    }

    public static void addClass(Token className){
        if(!symbolTable.containsKey(className.getLexeme())){
        symbolTable.put(className.getLexeme(), new ClassDeclaration(className));
        currentClass = symbolTable.get(className);
        }
        else{
            System.out.println("Class " + className.getLexeme() + " already exists");
        }
    }

    public static void addClass(ClassDeclaration classDeclaration){
        symbolTable.put(classDeclaration.getName().getLexeme(), classDeclaration);
        currentClass = classDeclaration;
    }

    public static void addAttribute(Token attribute, Token type){
        currentClass.addAttribute(attribute, type);
    }

    public static MethodDeclaration addMethod(Token method, Token returnType){
        return currentClass.addMethod(method, returnType);
    }

    public static String showString(){
        String print = "";
            for (HashMap.Entry<String, ClassDeclaration> entry : symbolTable.entrySet()) {

                ClassDeclaration classDeclaration = entry.getValue();
                print += classDeclaration.toString() + "\n";
            }
        return print;
    }

    public static MemberType decideType(String type){
        MemberType memberType ;
        switch (type){
            case "int":{
                memberType = IntegerType.getInstance();
                break;
            }
            case "char":{
                memberType = CharacterType.getInstance();
                break;
            }
            case "boolean":{
                memberType = BooleanType.getInstance();
                break;
            }
            case "String":{
                memberType = new MemberObjectType("String");
                break;
            }

            default:
                memberType = new MemberObjectType(type);
                break;
        }
        return memberType;
    }

    public static MethodDeclaration addConstructor(Token name) {
        return currentClass.addConstructor(name);
    }
}
