package lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    static void buildAndPrint(){
        Variable x = new Variable("x");
        Node exp = new Sum()
                .add(2.1,new Power(x, 3))
                .add(new Power(x, 2))
                .add(-2, x)
                .add(7);
        System.out.println(exp.toString());

    }
    static void buildAndEvaluate() {
        Variable x = new Variable("x");
        Node exp = new Sum()
                .add(new Power(x, 3))
                .add(-2, new Power(x, 2))
                .add(-1, x)
                .add(2);
        for (double v = -5; v < 5; v += 0.1) {
            x.setValue(v);
            System.out.printf(Locale.US, "f(%f)=%f\n", v, exp.evaluate());
        }
    }
    static void defineCircle(){
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x,2))
                .add(new Power(y,2))
                .add(8,x)
                .add(4,y)
                .add(16);
        System.out.println(circle.toString());

        List<String> listOfPointsInCircle = new ArrayList<>();
        
        while (listOfPointsInCircle.size()<100){
            double xv = 100*(Math.random()-.5);
            double yv = 100*(Math.random()-.5);
            x.setValue(xv);
            y.setValue(yv);

            double fv = circle.evaluate();
            if (fv<0) listOfPointsInCircle.add("(" + xv +", " +yv + ")");
        }

        for (String str : listOfPointsInCircle){
            System.out.println(str);
        }
    }

    static void diffPoly() {
        Variable x = new Variable("x");
        Node exp = new Sum()
                .add(2,new Power(x,3))
                .add(new Power(x,2))
                .add(-2,x)
                .add(7);
        System.out.print("exp=");
        System.out.println(exp.toString());

        Node d = exp.diff(x);
        System.out.print("d(exp)/dx=");
        System.out.println(d.toString());

    }

    static void diffCircle() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x,2))
                .add(new Power(y,2))
                .add(8,x)
                .add(4,y)
                .add(16);
        System.out.print("f(x,y)=");
        System.out.println(circle.toString());

        Node dx = circle.diff(x);
        System.out.print("d f(x,y)/dx=");
        System.out.println(dx.toString());
        System.out.print("d f(x,y)/dy=");
        Node dy = circle.diff(y);
        System.out.println(dy.toString());

    }

    static void expTest(){
        Variable x = new Variable("x");

        Exponential exp = new Exponential(2, x);
        System.out.println("f(x)= " + exp);

        x.setValue(3);
        double result = exp.evaluate();
        System.out.println("2^3 = " + result);

        Node derivative = exp.diff(x);
        System.out.println("d/dx (2^x) = " + derivative);

    }

    static void NaturalLogTest(){
        Node argument = new Constant(2);

        NaturalLogarithm ln = new NaturalLogarithm(argument);

        double result = ln.evaluate();
        System.out.println("ln(2) = " + result);

        Variable x = new Variable("x");
        Node derivative = new NaturalLogarithm(x)
                .diff(x);
        System.out.println("Derivative of ln(x) = " + derivative);

        System.out.println(ln);
    }

    public static void main(String[] args) {
        System.out.println("Test 1:");
        Main.buildAndPrint();
        System.out.println();

        System.out.println("Test 2:");
        Main.buildAndEvaluate();
        System.out.println();

        System.out.println("Test 3:");
        Main.defineCircle();
        System.out.println();

        System.out.println("Test 4:");
        Main.diffPoly();
        System.out.println();

        System.out.println("Test 5:");
        Main.diffCircle();
        System.out.println();

//        Main.expTest();
//        Main.NaturalLogTest();
    }

}
