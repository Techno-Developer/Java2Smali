package com.technodeveloper.java2smali.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class ToolsManager {
    public static void runJavac(String sourceFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "-source", "1.8", "-target", "1.8", sourceFile);
    }

    public static void runDx(String[] args) {
        com.android.dx.command.Main.main(args);
    }

    public static void runBaksmali(String[] args) {
        org.jf.baksmali.Main.main(args);
    }
}
