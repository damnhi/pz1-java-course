package lab5;

public class NaturalLogarithm extends Node{
    Node arg;

    NaturalLogarithm(Node arg){
        this.arg = arg;
    }
    NaturalLogarithm(double d){
        this(new Constant(d));
    }

    @Override
    double evaluate() {
        return Math.log(arg.evaluate());
    }

    @Override
    Node diff(Variable var) {
        Prod p = new Prod(new Power(arg,-1));
        p.mul(arg.diff(var));
        return p;
    }

    @Override
    boolean isZero() {
        return false;
    }

    public String toString() {
        String sgn=sign<0?"-":"";

        String b = sgn +
                "ln" +
                "(" +
                arg +
                ")";

        return b;
    }
}
