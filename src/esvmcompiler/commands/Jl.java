package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jl extends Instruction {
    public final static byte code = 23;
    public final static String asm = "Jl";

    public Jl() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jl(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
