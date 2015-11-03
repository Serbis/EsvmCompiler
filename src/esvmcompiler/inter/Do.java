package esvmcompiler.inter;

import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 27.10.15.
 */
public class Do extends Stmt {
    Expr expr;
    Stmt stmt;

    public Do() {
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in do");
    }

    public void gen(int b, int a) {
        after = a;
        int label = newlabel();
        stmt.gen(label, b);
        emitlabel(label);
        expr.jumping(b, 0);
    }
}
