package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void getImage(String filePath) {
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(filePath)));
        } catch(IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(screenX > -1 * gp.tileSize && screenY > -1 * gp.tileSize && screenX < gp.screenWidth && screenY < gp.screenHeight) {
            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
