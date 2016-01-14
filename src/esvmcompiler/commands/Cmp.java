package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Cmp extends Instruction {
    public static final byte code = 5;
    public final static String asm = "Cmp";

    public Cmp() {}

    public static void gen(int first, int second) {
        count++;
        Files.append(String.valueOf("Cmp(" + first + ", " + second +  ");\n"));
    }

    public static void genRaw(int first, int second) {

    }

}
