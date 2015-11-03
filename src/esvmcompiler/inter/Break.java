package esvmcompiler.inter;

/**
 * Created by serbis on 27.10.15.
 */
public class Break extends Stmt {
    public Stmt stmt;

    public Break() {
        if (Stmt.Encosing == null)
            error("unenclosed break");
        stmt = Stmt.Encosing;
    }

    public void gen(int b, int a) {
        emit("goto L" + stmt.after);
    }
}
