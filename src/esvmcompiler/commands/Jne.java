package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jne extends Instruction {
    public final static byte code = 25;
    public final static String asm = "Jne";

    public Jne() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jne(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
