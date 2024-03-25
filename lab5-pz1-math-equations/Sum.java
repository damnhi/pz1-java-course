package lab5;

import java.util.ArrayList;
import java.util.List;

public class Sum extends Node {

    List<Node> args = new ArrayList<>();

    public Sum(){}

    Sum(Node n1, Node n2){
        args.add(n1);
        args.add(n2);
    }


    Sum add(Node n){
        if(!n.isZero()) {
            args.add(n);
        }
        return this;
    }

    Sum add(double c){
        args.add(new Constant(c));
        return this;
    }

    Sum add(double c, Node n) {
        Node mul = new Prod(c,n);
        args.add(mul);
        return this;
    }

    @Override
    double evaluate() {
        double result = 0;
        for (Node arg : args) {
            double value = arg.evaluate();
            if (!arg.isZero()) {
                result += value;
            }
        }

        return sign*result;
    }

    int getArgumentsCount(){return args.size();}

    public String toString(){
        StringBuilder b =  new StringBuilder();
        if(sign<0)b.append("-(");

        for(int i = 0; i < args.size(); ++i){
            if(i!=0) b.append(" + ");
            b.append(args.get(i).toString());
        }

        if(sign<0)b.append(")");
        return b.toString();
    }


    @Override
    Node diff(Variable var) {
        Sum r = new Sum();
        for(Node n:args){
            if(!n.isZero()) {
                r.add(n.diff(var));
            }
        }
        if (r.args.isEmpty()){
            return new Constant(0);
        }
        return r;
    }

    @Override
    boolean isZero() {
        for (Node arg : args){
            if (!arg.isZero()){
                return false;
            }
        }
        return true;
    }



}