package lab5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Prod extends Node {
    List<Node> args = new ArrayList<>();

    Prod(){}

    Prod(Node n1){
        args.add(n1);
        this.simplify();
    }

    Prod(double c){
        this(new Constant(c));
        this.simplify();
    }

    Prod(Node n1, Node n2){
        args.add(n1);
        args.add(n2);
        this.simplify();
    }

    Prod(double c, Node n){
        this(new Constant(c),n);
    }


    Prod mul(Node n){
        args.add(n);
        this.simplify();
        return this;
    }

    Prod mul(double c){
        args.add(new Constant(c));
        this.simplify();
        return this;
    }

    @Override
    double evaluate() {
        double result =1;
        for(Node arg : args) {
            if (arg.isZero()) {
                return 0;
            }
            result *= arg.evaluate();
        }
        return sign*result;
    }

    int getArgumentsCount(){return args.size();}

    public String toString(){
        StringBuilder b =  new StringBuilder();
        if(sign<0)b.append("-");
        for(int i = 0; i < args.size(); ++i){
            Node arg = args.get(i);
            boolean isNegative = arg.sign < 0;
            String strToAppend = arg.toString();

            if(i!=0) b.append("*");

            if(isNegative) strToAppend = "(" + arg.toString() + ")";

            b.append(strToAppend);
        }
        return b.toString();
    }

    @Override
    Node diff(Variable var) {

        Sum r = new Sum();
        for(int i=0;i<args.size();i++){
            Prod m= new Prod();
            for(int j=0;j<args.size();j++){
                Node f = args.get(j);
                if(j==i)m.mul(f.diff(var));
                else m.mul(f);
            }
            m.simplify();
            r.add(m);
        }
        if (r.args.isEmpty()) {
            return new Constant(0);
        }
        return r;
    }

    @Override
    boolean isZero() {
        for (Node arg : args){
            if (arg.isZero()){
                return true;
            }
        }
        return false;
    }

    void simplify(){
        List<Node> listOfConst = new ArrayList<>();

        Iterator<Node> iterator = args.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof Constant) {
                listOfConst.add(node);
                iterator.remove();
            }
        }

        double trueConstValue = 1;
        for (Node constant : listOfConst){
            trueConstValue *= constant.evaluate();
        }

        Constant trueConstant = new Constant(trueConstValue);
        args.add(0, trueConstant);

    }


}