import java.awt.*;

public class Bubble implements XmasShape {
    int x;
    int y;
    double scale;
    Color lineColor = new Color(0,0,0);
    Color fillColor;

    Bubble(int x, int y, double scale, Color fillColor){
        super();
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.fillColor = fillColor;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setPaint(fillColor);
        g2d.fillOval(0,0,20,20);
        g2d.setPaint(lineColor);
        g2d.drawOval(0,0,20,20);
    }

    @Override
    public void transform(Graphics2D g2d) {
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
