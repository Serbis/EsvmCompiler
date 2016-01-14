package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Push extends Instruction {
    public final static byte code = 3;
    public final static String asm = "Push";

    public Push() {}

    public static void gen(int type, String hex) {
        count++;
        Files.append(String.valueOf("Push(" + type + ", " + hex + ");\n"));
    }

    public static void genRaw(int type, String hex) {

    }

}
