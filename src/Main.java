import LexicalAnalyzer.LexicalAnalyzerImp;
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

        System.out.println(lexicalAnalyzer.nextToken());

        System.out.println("[SinErrores]");

    }
}