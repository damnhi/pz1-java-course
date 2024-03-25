package lab5;

public class Exponential extends Node{
    double arg;
    Node variable;

    Exponential(double arg, Node variable){
        this.arg = arg;
        this.variable = variable;
    }

    @Override
    double evaluate() {
        double varValue = variable.evaluate();
        return Math.pow(arg, varValue);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        String sgn=sign<0?"-":"";
        b.append(sgn);
        b.append(arg);
        b.append("^");

        int varSign = variable.getSign();
        int cnt = variable.getArgumentsCount();
        String varString = variable.toString();

        boolean useBracket = varSign < 0 || cnt > 1;

        if(useBracket)b.append("(");
        b.append(varString);
        if(useBracket)b.append(")");

        return b.toString();
    }


    @Override
    Node diff(Variable var) {
        Exponential exp = new Exponential(arg, variable);

        Prod p = new Prod(new Constant(Math.log(arg)),exp);
        p.mul(variable.diff(var));

        return p;
    }

    @Override
    boolean isZero() {
        return arg==0;
    }
}
