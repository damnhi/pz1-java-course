import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Zombie");;
        DrawPanel panel = new DrawPanel(Main.class.getResource("6473205443_7df3397e72_b.jpg"),ZombieFactory.INSTANCE);
        frame.setContentPane(panel);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                panel.stopAnimation = true;
                panel.crossHair.timer.cancel();
                System.out.println("Zamknięto wątki");
            }
        });

    }
}