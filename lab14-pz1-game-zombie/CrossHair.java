import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

public class CrossHair implements MouseMotionListener, MouseListener {

    DrawPanel parent;
    Timer timer = new Timer("Timer");

    CrossHair(DrawPanel parent) {
        this.parent = parent;

        parent.setCursor( parent.getToolkit().createCustomCursor(
                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null ) );
    }

    /* x, y to współrzedne celownika
       activated - flaga jest ustawiana po oddaniu strzału (naciśnięciu przyciku myszy)
    */
    int x;
    int y;
    boolean activated = false;
    List<CrossHairListener> listeners = new ArrayList<CrossHairListener>();

    void draw(Graphics g){
        if(activated)g.setColor(Color.RED);
        else g.setColor(Color.WHITE);

        g.drawLine(x - 25, y, x + 25, y);
        g.drawLine(x, y - 25, x, y + 25);

        g.drawOval(x - 5 , y - 5, 10, 10);
        g.drawOval(x - 15 , y - 15, 30, 30);
    }

    void addCrossHairListener(CrossHairListener e){
        listeners.add(e);
    }

    void notifyListeners(){
        for(var e:listeners)
            e.onShotsFired(x,y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        activated = true;
        notifyListeners();
        parent.repaint();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activated=false;
                parent.repaint();
            }
        },300);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        parent.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        parent.repaint();
    }
}