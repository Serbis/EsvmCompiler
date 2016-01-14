package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Mul extends Instruction {
    public final static byte code = 8;
    public final static String asm = "Mul";

    public Mul() {}

    public static void gen() {
        count++;
        Files.append(String.valueOf("Mul();\n"));
    }

    public static void genRaw() {

    }

}
