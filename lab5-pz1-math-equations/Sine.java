package lab5;

public class Sine extends Node{
    Node arg;

    Sine(Node arg){
        this.arg = arg;
    }
    Sine(double d){
        this(new Constant(d));
    }

    @Override
    double evaluate() {
        return Math.sin(arg.evaluate());
    }

    @Override
    Node diff(Variable var) {
        Prod p = new Prod(new Cosine(arg));
        p.mul(arg.diff(var));
        return p;
    }

    @Override
    boolean isZero() {
        return arg.isZero() || arg.evaluate() % Math.PI == 0;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        String sgn=sign<0?"-":"";
        b.append(sgn);
        b.append("sin");

        b.append("(");
        b.append(arg);
        b.append(")");

        return b.toString();
    }
}
