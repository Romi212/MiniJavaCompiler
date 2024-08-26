import LexicalAnalyzer.LexicalAnalyzerImp;
import utils.Token;
import utils.sourcemanager.SourceManagerImpl;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please provide a file name as an argument.");
            return;
        }

        String fileName = args[0];

        System.out.println(fileName);

        SourceManagerImpl sourceManager = new SourceManagerImpl();
        LexicalAnalyzerImp lexicalAnalyzer = new LexicalAnalyzerImp(sourceManager);
        try {
            sourceManager.open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Token token;
       do{
            token = lexicalAnalyzer.nextToken();
              System.out.println(token);
       }while(token.getToken() != "EOF");


        System.out.println("[SinErrores]");

    }
}