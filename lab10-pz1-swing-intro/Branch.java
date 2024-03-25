import java.awt.*;
import java.awt.geom.AffineTransform;

public class Branch implements XmasShape {
    int x;
    int y;
    double scale;
    GradientPaint fillColor;
    boolean withTreeTrunk;

    Branch(int x, int y, double scale, GradientPaint fillColor, boolean withTreeTrunk){
        super();
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.fillColor = fillColor;
        this.withTreeTrunk = withTreeTrunk;
    }

    void drawTreeTrunk(Graphics2D g2d){
        g2d.setColor(new Color(102,51,0));
        g2d.fillRect(-25, 70, 50, 80);
    }



    @Override
    public void render(Graphics2D g2d) {
        AffineTransform mat = g2d.getTransform();
        if (withTreeTrunk){
            drawTreeTrunk(g2d);
        }
        g2d.setPaint(fillColor);
        int a[]={0,0,-136,-286};
        int b[]={0,110,89,108,};
        g2d.fillPolygon(a,b,a.length);
        g2d.scale(-1,1);
        g2d.fillPolygon(a,b,a.length);
        g2d.setTransform(mat);
    }

    @Override
    public void transform(Graphics2D g2d) {
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
