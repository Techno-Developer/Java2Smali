package com.technodeveloper.java2smali;

import com.technodeveloper.java2smali.util.ToolsManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String VERSION = "1.2";

    private static final String HELP_MESSAGE = "Usage:" +
            "\n\tJava2Smali <JavaSourceFile>" +
            "\n\tJava2Smali --version" +
            "\n\tJava2Smali --help";
    private static final String NOFILE_ERROR = "[E] $FILE$ does not exist.";
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
            if (args[0].startsWith("--")) {
                if (args[0].equals("--version") || args[0].equals("--v")) {
                    print("Java2Smali v" + VERSION);
                } else if (args[0].equals("--help") || args[0].equals("--h")) {
                    print(HELP_MESSAGE);
                } else {
                    print("[E] Unknown command");
                    print(HELP_MESSAGE);
                }
            } else if (new File(args[0]).exists()) {
                convert(args[0]);
                extractAndClean();
                print("[I] Done.");
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

        print("[I] Compiling...");
        String errors = ToolsManager.runJavac(javaFileName);
        if (errors.contains("error")) {
            print("[E] Error during the compilation of ".concat(javaFileName).concat("\n").concat(errors));
            print("[E] Compilation failed, check errors above for more info.");
            System.exit(1);
        }
        print("[I] Compiled successfully.");

        print("[I] Dexing...");
        ToolsManager.runDx(new String[]{"--dex", "--output", dexFileName, javaClassFileName});

        print("[I] Running baksmali...");
        ToolsManager.runBaksmali(new String[]{"d", dexFileName});
    }

    private void extractAndClean() {
        print("[I] Copying smali source...");
        File smaliFile = new File("out/".concat(smaliFileName));

        try {
            File oldSmaliFile = new File(smaliFileName);
            if (oldSmaliFile.exists()) {
                print("[I] Deleting duplicate...");
                deleteFile(oldSmaliFile);
            }
            Files.move(Paths.get(smaliFile.toURI()), Paths.get(smaliFileName));
        } catch (IOException e) {
            print("[E] Error during extraction.");
            System.exit(1);
        }

        print("[I] Deleting junk files...");
        deleteFile("out");
        deleteFile(javaClassFileName);
        deleteFile(dexFileName);
    }

    private static void deleteFile(String fileName) {
        deleteFile(new File(fileName));
    }

    private static void deleteFile(File file) {
        if (!file.delete()) {
            print("[E] Cannot delete ".concat(file.getName()).concat("."));
        }
    }

    private static void print(String s) {
        System.out.println(s);
    }
}
