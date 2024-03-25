import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class DrawPanel  extends JPanel implements CrossHairListener {
    BufferedImage background;
    final List<Sprite> sprites = new ArrayList<>();
    SpriteFactory factory;
    CrossHair crossHair;
    StatPanel statsPanel;

    volatile boolean stopAnimation = false;

    DrawPanel(URL backgroundImagageURL, SpriteFactory factory) {
        try {
            background = ImageIO.read(backgroundImagageURL);
            crossHair = new CrossHair(this);
            crossHair.addCrossHairListener(this);
            addMouseListener(crossHair);
            addMouseMotionListener(crossHair);
            statsPanel = new StatPanel(this);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.factory = factory;
        AnimationThread animationThread = new AnimationThread();
        animationThread.start();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        statsPanel.draw(g2d);
        synchronized (sprites) {
            sprites.sort((s1, s2) -> {
                if (s1 instanceof Zombie && s2 instanceof Zombie) {
                    return (s1).isCloser(s2) ? -1 : 1;
                }
                return 0;
            });

            for (Sprite zombie : sprites) {
                zombie.draw(g2d, this);
            }

            Iterator<Sprite> iterator = sprites.iterator();
            while (iterator.hasNext()) {
                Sprite sprite = iterator.next();
                if (sprite instanceof Zombie zombie) {
                    if (!zombie.isVisible()) {
                        iterator.remove();
                        statsPanel.updateMissed();
                    }
                }
            }
        }
        crossHair.draw(g2d);
    }

    @Override
    public void onShotsFired(int x, int y) {
        for (int i = sprites.size() - 1; i >= 0; i--) {
            Sprite sprite = sprites.get(i);
            if (sprite instanceof Zombie zombie) {
                if (zombie.isHit(x, y)) {
                    sprites.remove(i);
                    statsPanel.updateKilled();
                    break;
                }
            }
        }
    }

    class AnimationThread extends Thread {
        public void run() {
                for (int i = 1;!stopAnimation ; i++) {
                    synchronized (sprites) {
                        for (Sprite zombie : sprites) {
                            zombie.next();
                        }

                        if (i % 30 == 0) {
                            sprites.add(factory.newSprite(getWidth(), (int) (0.7 * getHeight())));
                        }
                    }

                    SwingUtilities.invokeLater(DrawPanel.this::repaint);
                    try {
                        sleep(1000 / 30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }

    }
}