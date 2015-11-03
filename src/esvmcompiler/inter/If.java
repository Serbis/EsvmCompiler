package esvmcompiler.inter;

import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 27.10.15.
 */
public class If extends Stmt {
    public Expr expr;
    public Stmt stmt;

    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in if");
    }

    public void gen(int b, int a) {
        int label = newlabel();
        expr.jumping(0, a);
        emitlabel(label);
        stmt.gen(label, a);
    }
}
