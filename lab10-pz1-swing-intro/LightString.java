import java.awt.*;

public class LightString implements XmasShape {
    int x;
    int y;
    int numberOfLights;
    double scale;

    LightString(int x, int y, double scale, int numberOfLights){
        super();
        this.x = x;
        this.y = y;
        this.numberOfLights = numberOfLights;
        this.scale = scale;
    }

    public void drawOneLightLine(int[] currentState,int numOfLights, Graphics2D g2d, boolean left){

        int currentX = currentState[0];
        int currentY = currentState[1];
        for (int i = 0; i < numOfLights; i++){
            if (left)
                currentX = currentX - 20;
            else
                currentX = currentX + 20;
            currentY = currentY - 5;
            new Light(currentX, currentY, scale).draw(g2d);
        }
        currentState[0] = currentX;
        currentState[1]= currentY;

    }

    @Override
    public void render(Graphics2D g2d) {
        int[] holderForCurrent = {x, y};
        drawOneLightLine(holderForCurrent,numberOfLights ,g2d,true);
        drawOneLightLine(holderForCurrent,numberOfLights-3 ,g2d,false);
        drawOneLightLine(holderForCurrent,numberOfLights-4, g2d,true);
        drawOneLightLine(holderForCurrent,numberOfLights-5, g2d,false);
    }

    @Override
    public void transform(Graphics2D g2d) {
        g2d.translate(x,y);
        g2d.scale(scale,scale);
    }
}
