package esvmcompiler.inter;

import esvmcompiler.lexer.Tag;
import esvmcompiler.lexer.Word;
import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 26.10.15.
 */
public class Assess extends Op{
    public Id array;
    public Expr index;

    public Assess(Id a, Expr i, Type p) {
        super(new Word("[]", Tag.INDEX), p);
        array = a;
        index = i;
    }

    public Expr gen() {
        return new Assess(array, index.reduce(), type);
    }

    public void jumping(int t, int f) {
        emitjumps(reduce().toString(), t, f);
    }

    public String toString() {
        return array.toString() + " [ " + index.toString() + " ] ";
    }

}
