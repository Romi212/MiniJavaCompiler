package SemanticAnalyzer;

import java.util.HashMap;


public class SymbolTable {
    static private HashMap<String,ClassDeclaration> symbolTable = new HashMap<>();
    static private ClassDeclaration currentClass;

    public static void addClass(String className){
        symbolTable.put(className, new ClassDeclaration(className));
        currentClass = symbolTable.get(className);
    }

    public static void addClass(ClassDeclaration classDeclaration){
        symbolTable.put(classDeclaration.getName(), classDeclaration);
        currentClass = classDeclaration;
    }

    public static void addAttribute(String attribute, String type){
        currentClass.addAttribute(attribute, type);
    }

    public static void addMethod(String method, String returnType){
        currentClass.addMethod(method, returnType);
    }

    public static String showString(){
        String print = "";
            for (HashMap.Entry<String, ClassDeclaration> entry : symbolTable.entrySet()) {
                String className = entry.getKey();
                ClassDeclaration classDeclaration = entry.getValue();
                print += classDeclaration.toString() + "\n";
            }
        return print;
    }
}
