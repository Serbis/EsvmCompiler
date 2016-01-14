package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Div extends Instruction {
    public  final static byte code = 17;
    public final static String asm = "Div";

    public Div() {}

    public static void gen() {
        count++;
        Files.append(String.valueOf("Div();\n"));
    }

    public static void genRaw() {

    }

}
