package esvmcompiler.inter;

/**
 * Created by serbis on 27.10.15.
 */
public class Stmt extends Node{
    public static Stmt Null = new Stmt();
    public int after = 0;
    public static Stmt Encosing = Stmt.Null;

    public Stmt() {}

    public void gen(int b, int a) {

    }
}
