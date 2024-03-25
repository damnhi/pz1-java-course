package lab5;

public class Cosine extends Node{
    Node arg;

    Cosine(Node arg){
        this.arg = arg;
    }
    Cosine(double d){
        this(new Constant(d));
    }

    @Override
    double evaluate() {
        return Math.cos(arg.evaluate());
    }

    @Override
    Node diff(Variable var) {
        Prod p = new Prod(-1,new Sine(arg));
        p.mul(arg.diff(var));
        return p;
    }

    @Override
    boolean isZero() {
        return arg.evaluate() % (Math.PI/2) == 0;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        String sgn=sign<0?"-":"";
        b.append(sgn);
        b.append("cos");

        b.append("(");
        b.append(arg);
        b.append(")");

        return b.toString();
    }
}
