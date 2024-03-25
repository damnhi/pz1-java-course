import javax.swing.*;
import java.awt.*;

class StatPanel {
    private int missedZombies = 0;
    private int killedZombies = 0;
    DrawPanel drawPanel;

    StatPanel(DrawPanel drawPanel){
        this.drawPanel = drawPanel;
    }

    public void updateMissed() {
        missedZombies ++;
        drawPanel.repaint();
    }
    public void updateKilled() {
        killedZombies ++;
        drawPanel.repaint();
    }

    protected void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.drawString("Missed Zombies: " + missedZombies, 10, 15);
        g2d.setColor(Color.RED);
        g2d.drawString("Killed Zombies: " + killedZombies, 10, 30);
    }
}