package SemanticAnalyzer;

public class MethodDeclaration {

    private String name;
    private String returnType;

    public MethodDeclaration(String name, String returnType){
        this.name = name;
        this.returnType = returnType;
    }

    public String getName(){
        return this.name;
    }
    public String getReturnType(){
        return this.returnType;
    }
}
