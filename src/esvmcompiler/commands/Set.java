package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Set extends Instruction {
    public final static byte code = 32;
    public final static String asm = "Set";

    public Set() {}

    public static void gen(int number, int size, String hex) {
        count++;
        Files.append(String.valueOf("Set(" + number + ", " + size + ", " + hex + ");\n"));
    }

    public static void genRaw(int number, int size, String hex) {

    }

}
