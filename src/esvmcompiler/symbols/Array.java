package esvmcompiler.symbols;

import esvmcompiler.lexer.Tag;

/**
 * Created by serbis on 26.10.15.
 */
public class Array extends Type {
    public Type of;
    public int size = 1;

    public Array(int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width, p.code);
        size = sz;
        of = p;
    }

    public String toString() {
        return "[" + size + "]" + of.toString();
    }
}
