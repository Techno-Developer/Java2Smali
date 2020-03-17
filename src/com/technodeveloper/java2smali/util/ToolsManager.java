package com.technodeveloper.java2smali.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.OutputStream;

public class ToolsManager {
    public static String runJavac(String sourceFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        OutputStream errors = new OutputStream() {
            private StringBuilder sb = new StringBuilder();

            @Override
            public void write(int b) {
                this.sb.append((char) b);
            }

            @Override
            public String toString() {
                return this.sb.toString();
            }
        };

        compiler.run(null, null, errors, "-source", "1.8", "-target", "1.8", sourceFile);
        return errors.toString();
    }

    public static void runDx(String[] args) {
        com.android.dx.command.Main.main(args);
    }

    public static void runBaksmali(String[] args) {
        org.jf.baksmali.Main.main(args);
    }
}
