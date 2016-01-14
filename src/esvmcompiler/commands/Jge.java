package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jge extends Instruction {
    public final static byte code = 22;
    public final static String asm = "Jge";

    public Jge() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jge(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
