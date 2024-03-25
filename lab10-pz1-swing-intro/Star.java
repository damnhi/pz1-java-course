import java.awt.*;
import java.awt.geom.AffineTransform;

public class Star implements XmasShape {
    int x;
    int y;
    double scale;

    Star(int x, int y, double scale){
        super();
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(new Color(217, 217, 33));
        AffineTransform mat = g2d.getTransform();
        int starWidth = 45;
        int center_point_y = (int) (starWidth/2 * Math.sqrt(3))/3;
        int x[]={-starWidth/2, 0, starWidth/2};
        int y[]={-center_point_y, 2 * center_point_y, -center_point_y};
        for(int i=0;i<2;i++){
            g2d.fillPolygon(x,y,x.length);
            g2d.rotate(2*Math.PI/2);
        }
        g2d.setTransform(mat);

    }

    @Override
    public void transform(Graphics2D g2d) {
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
