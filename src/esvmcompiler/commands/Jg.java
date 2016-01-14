package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jg extends Instruction {
    public final static byte code = 21;
    public final static String asm = "Jg";

    public Jg() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jg(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
