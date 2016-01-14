package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Pop extends Instruction {
    public final static byte code = 4;
    public final static String asm = "Pop";

    public Pop() {}

    public static void gen(int number) {
        count++;
        Files.append(String.valueOf("Pop(" + number +  ");\n"));
    }

    public static void genRaw(int number) {

    }

}
