package esvmcompiler.inter;

import esvmcompiler.symbols.Array;
import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 27.10.15.
 */
public class SetElem extends Stmt {
    public Id array;
    public Expr index;
    public Expr expr;

    public SetElem(Assess x, Expr y) {
        array = x.array;
        index = x.index;
        expr = y;
        if (check(x.type, expr.type) == null)
            error("type error");
    }

    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array && p2 instanceof Array)
            return null;
        else if (p1 == p2)
            return p2;
        else if (Type.nuberic(p2) && Type.nuberic(p2))
            return p2;
        else
            return null;
    }

    public void gen(int b, int a) {
        String s1 = index.reduce().toString();
        String s2 = expr.reduce().toString();
        emit(array.toString() + " [ " + s1 + " ] = " + s2);
    }


}
