package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CelVMCodeGenerator {

    private static final String OUTPUT_FILE = "output.celvm";
    private static BufferedWriter writer;
    private static File file = createFile();

    // Method to create a new file if it does not exist and initialize BufferedWriter
    public static File createFile() {
        File file = null;
        try {
            file = new File(OUTPUT_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // Method to add a sentence to the file
    public static void add(String sentence) {
        try {
            writer.write(sentence);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Method to close the BufferedWriter
    public static void closeWriter() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}