import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Zombie implements Sprite{
    BufferedImage tape;
    int x = 500;
    int y = 500;
    double scale = 1;

    int index = 0;
    int HEIGHT = 310;
    int WIDTH = 200;

    Zombie(int x, int y, double scale, BufferedImage tape) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.tape = tape;
    }


    /**
     * Pobierz odpowiedni podobraz klatki (odpowiadającej wartości zmiennej index)
     * i wyświetl go w miejscu o współrzędnych (x,y)
     *
     * @param g
     * @param parent
     */

    public void draw(Graphics g, JPanel parent) {
        Image img = tape.getSubimage(index * WIDTH, 0, WIDTH, HEIGHT);
        g.drawImage(img, x, y - (int) (HEIGHT * scale) / 2, (int) (WIDTH * scale), (int) (HEIGHT * scale), parent);
    }

    /**
     * Zmień stan - przejdź do kolejnej klatki
     */

    public void next() {
        x -= 20 * scale;
        index = (index + 1) % 10;
    }

    public boolean isVisible() {
        int panelWidth = tape.getWidth();
        return x + WIDTH * scale > 0 && x < panelWidth;
    }

    public boolean isHit(int _x, int _y){
        int scaledWidth = (int) (WIDTH * scale);
        int scaledHeight = (int) (HEIGHT * scale);
        return _x >= x && _x <= x + scaledWidth && _y >= y - scaledHeight / 2 && _y <= y + scaledHeight / 2;
    }

    public boolean isCloser(Sprite other) {
        if (other instanceof Zombie otherZombie) {
            return otherZombie.scale > this.scale;
        }
        return false;
    }
}