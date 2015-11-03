package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Push extends Instruction {
    public Push() {}



    public static void gen(int type, String hex) {
        count++;
        Files.append(String.valueOf("Push(" + type + ", " + hex + ");\n"));
    }

}
