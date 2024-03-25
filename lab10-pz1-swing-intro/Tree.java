import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Tree implements XmasShape {
    int x;
    int y;
    double scale;

    AffineTransform saveAt;
    GradientPaint fillColorOfBranches;
    List<Branch> branchList = new ArrayList<>();

    Tree(int x, int y, double scale, GradientPaint fillColorOfBranches){
        super();
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.fillColorOfBranches = fillColorOfBranches;
        addBranches();
    }
    void addBranches(){
        for (int i = 0; i < 9 ; i++){
            boolean withTreeTrunk = i == 0;
            double newScale = scale /  Math.log(i + 4);
            Branch branchToBeAdded = new Branch(x,y - 20 * i,newScale,fillColorOfBranches, withTreeTrunk);
            branchList.add(branchToBeAdded);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        for(Branch branch : branchList){
            branch.draw(g2d);
        }
    }

    @Override
    public void transform(Graphics2D g2d) {
        saveAt = g2d.getTransform();
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}