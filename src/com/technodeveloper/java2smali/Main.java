package com.technodeveloper.java2smali;

import com.technodeveloper.java2smali.util.ToolsManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String HELP_MESSAGE = "Usage:\n\tJava2Smali <JavaSourceFile>";
    private static final String NOFILE_ERROR = "$FILE$ does not exist.";
    private static final String JAVA_EXT = ".java";
    private static final String CLASS_EXT = ".class";
    private static final String SMALI_EXT = ".smali";

    private String javaFileName;
    private String javaClassFileName;
    private String smaliFileName;
    private String dexFileName;

    public static void main(String[] args) {
        new Main().init(args);
    }

    private void init(String[] args) {
        if (args.length == 0) {
            print(HELP_MESSAGE);
        } else {
            if (new File(args[0]).exists()) {
                convert(args[0]);
                extractAndClean();
            } else {
                print(NOFILE_ERROR.replace("$FILE$", args[0]));
            }
        }
    }

    private void convert(String fileName) {
        javaFileName = fileName;
        smaliFileName = javaFileName.replace(JAVA_EXT, SMALI_EXT);
        javaClassFileName = javaFileName.replace(JAVA_EXT, CLASS_EXT);
        dexFileName = "classes.dex";

        print("Compiling...");
        ToolsManager.runJavac(javaFileName);

        print("Dexing...");
        ToolsManager.runDx(new String[]{"--dex", "--output", dexFileName, javaClassFileName});

        print("Running baksmali...");
        ToolsManager.runBaksmali(new String[]{"d", dexFileName});
    }

    private void extractAndClean() {
        print("Copying smali source...");
        File smaliFile = new File("out/".concat(smaliFileName));

        try {
            Files.move(Paths.get(smaliFile.toURI()), Paths.get(smaliFileName));
        } catch (IOException e) {
            print("Error during extraction");
            System.exit(1);
        }

        print("Deleting junk files...");
        deleteFile("out");
        deleteFile(javaClassFileName);
        deleteFile(dexFileName);
    }

    private static void deleteFile(String fileName) {
        if (!new File(fileName).delete()) {
            print("Cannot delete ".concat(fileName));
        }
    }

    private static void print(String s) {
        System.out.println(s);
    }
}