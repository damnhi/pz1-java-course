import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DrawPanel extends JPanel {
    List<XmasShape> shapes = new ArrayList<>();
    Image backgroundImage;

    public DrawPanel() throws IOException {
        backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("ChristmasPZ2.jpeg")));

        Tree tree = new Tree(200,150,1,new GradientPaint(0,0,new Color(19, 150, 26),0,100, new Color(2, 38, 5)));
        Star star = new Star(400,140,1);
        LightString lightString= new LightString(260,170,1,11);

        addXmasObject(tree);
        addXmasObject(star);
        addXmasObject(lightString);

        Random rand = new Random();
        for (int j = 0; j<3; j++) {
            int bubbleX = 330;
            if (j == 0){
                bubbleX = 270;
            }
            int bubbleY = 320;
            int randomShift = 0;
            for (int i = 0; i < 6-j; i++) {
                Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                randomShift = rand.nextInt(40) - 20 - 70*j;
                Bubble bubble = new Bubble(bubbleX + i*40, bubbleY + randomShift, 1, color);
                addXmasObject(bubble);
            }
        }
//
    }

    public void addXmasObject(XmasShape xmasShape){
        shapes.add(xmasShape);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        for(XmasShape s:shapes){
            s.draw((Graphics2D)g);
        }

    }

}
