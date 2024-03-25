import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;

public class ClockWithGui extends JPanel {

    LocalTime time = LocalTime.now();
    public void paintComponent(Graphics g){
        Graphics2D g2d=(Graphics2D)g;
        Font  font  = new Font(Font.SANS_SERIF,  Font.BOLD, 12);
        g2d.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textWidth = 0;
        int textHeight = fontMetrics.getHeight();;

        g2d.translate(getWidth()/2,getHeight()/2);
        g2d.fillOval(-5,-5, 10,10);
        for(int i=1;i<13;i++){
            AffineTransform at = new AffineTransform();

            at.rotate(2*Math.PI/12*i);
            Point2D src = new Point2D.Float(0,-120);
            Point2D trg = new Point2D.Float();
            at.transform(src,trg);

            textWidth = fontMetrics.stringWidth(Integer.toString(i));

            g2d.drawString(Integer.toString(i),(int)trg.getX() - textWidth / 2,(int)trg.getY() + textHeight/2);
        }

        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawOval(-140,-140, 280,280);
        drawClockLines(g2d, "HOUR");
        drawClockLines(g2d, "MINUTE");

        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        double hourPos = hour%12*2*Math.PI/12 + minute*Math.PI/(6*60)+ second*Math.PI/(360*60);
        double minutePos = minute* Math.PI/30 + second*Math.PI/(30*60);
        double secondPos = second*Math.PI/30;

        drawHand(g2d,hourPos,-90,4.0f);
        drawHand(g2d,minutePos,-120,2.0f);
        drawHand(g2d,secondPos,-120,1.0f);
    }

    public void drawHand(Graphics g, double timePosition, int length, float width){
        Graphics2D g2d=(Graphics2D)g;

        AffineTransform saveAT = g2d.getTransform();
        g2d.rotate(timePosition);
        g2d.setStroke(new BasicStroke(width));
        g2d.drawLine(0,0,0,length);
        g2d.setTransform(saveAT);
    }

    public void drawClockLines(Graphics g, String specify){
        Graphics2D g2d=(Graphics2D)g;
        int numOfIterations = 0;
        float width = 0;
        int length = 0;
        double theta = 0.0;

        switch (specify) {
            case "HOUR" -> {
                numOfIterations = 12;
                width = 3.0f;
                length = 10;
                theta = 2 * Math.PI / 12;
            }
            case "MINUTE" -> {
                numOfIterations = 60;
                width = 2.0f;
                length = 5;
                theta = 2 * Math.PI / 60;
            }
            default -> {
            }
        }

        AffineTransform saveAT = g2d.getTransform();

        for(int i=0;i<numOfIterations;i++){
            g2d.setStroke(new BasicStroke(width));
            g2d.drawLine(0,140 - length,0,140);
            g2d.rotate(theta);
        }

        g2d.setTransform(saveAT);
    }

    class ClockThread extends Thread{
        @Override
        public void run() {
            for(;;){
                time = LocalTime.now();
                System.out.printf("%02d:%02d:%02d\n",time.getHour(),time.getMinute(),time.getSecond());

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                repaint();
            }
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clock");
        ClockWithGui clockWithGui = new ClockWithGui();
        frame.setContentPane(clockWithGui);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        ClockThread clockThread = clockWithGui.new ClockThread();
        clockThread.start();

    }
}