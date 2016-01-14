package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Pushv extends Instruction {
    public final static byte code = 33;
    public final static String asm = "Pushv";

    public Pushv() {}

    public static void gen(int number) {
        count++;
        Files.append(String.valueOf("Pushv(" + number +  ");\n"));
    }

    public static void genRaw(int number) {

    }

}
