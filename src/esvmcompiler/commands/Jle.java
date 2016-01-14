package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jle extends Instruction {
    public final static byte code = 24;
    public final static String asm = "Jle";

    public Jle() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jle(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
