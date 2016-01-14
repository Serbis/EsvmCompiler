package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Add extends Instruction {
    public final static byte code = 1;
    public final static String asm = "Add";

    public Add() {}

    public static void gen() {
        count++;
        Files.append(String.valueOf("Add();\n"));
    }

    public static void genRaw() {

    }

}
