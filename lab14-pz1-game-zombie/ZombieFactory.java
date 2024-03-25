import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public enum ZombieFactory implements SpriteFactory{
    INSTANCE;

    private BufferedImage tape;

    ZombieFactory() {
        try {
            tape = ImageIO.read(getClass().getResource("walkingdead.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sprite newSprite(int x,int y){
        Random random = new Random();
        double scale = 0.2 + 1.8* random.nextDouble();
        return new Zombie(x,y,scale,tape);
    }

}
