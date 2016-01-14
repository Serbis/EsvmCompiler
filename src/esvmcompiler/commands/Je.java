package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Je extends Instruction {
    public final static byte code = 19;
    public final static String asm = "Je";

    public Je() {}

    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Je(" + pos +  ");\n"));
    }

    public static void genRaw(int pos) {

    }

}
