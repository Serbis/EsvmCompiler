package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Add extends Instruction {
    public Add() {}



    public static void gen() {
        count++;
        Files.append(String.valueOf("Add();\n"));
    }

}
