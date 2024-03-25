import java.awt.*;

public class Light implements XmasShape {
    int x;
    int y;
    double scale;
    Color fillColor;

    Light(int x, int y, double scale, Color fillColor){
        super();
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.fillColor = fillColor;
    }
    Light(int x, int y, double scale){
        this(x,y,scale,new Color(255,255,0));
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setPaint(fillColor);
        g2d.fillOval(0,0,10,10);
        g2d.setPaint(new Color(0,0,0));
        g2d.drawOval(0,0,10,10);
    }

    @Override
    public void transform(Graphics2D g2d) {
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}

