package esvmcompiler.asmgen;

import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 29.10.15.
 */
public class Var {
    public String name;
    public Type type;
    public int born;
    public int death;
    public boolean destroy = false;

    public Var() {}

    public Var (String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Var (int born, int death) {
        this.born = born;
        this.death = death;
    }

    public Var (int born) {
        this.born = born;
    }

    public Var (String name, int born) {
        this.born = born;
        this.name = name;
    }
}
