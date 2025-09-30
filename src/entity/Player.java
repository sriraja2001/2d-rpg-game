package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 6;
        solidArea.y = gp.tileSize / 3;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = (2 * gp.tileSize) / 3;
        solidArea.height = (2 * gp.tileSize) / 3;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        // denotes the starting position of the player in the world, and not on the screen
        worldX =  gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "up";
     }

    public void getPlayerImage() {
        try {
            up1 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_up_1.png")));
            up2 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_up_2.png")));
            down1 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_down_1.png")));
            down2 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_down_2.png")));
            left1 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_left_1.png")));
            left2 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_left_2.png")));
            right1 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_right_1.png")));
            right2 =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_right_2.png")));
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void update() {
        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if(keyHandler.upPressed) {
                direction = "up";
            }
            else if(keyHandler.downPressed) {
                direction = "down";
            }
            else if(keyHandler.leftPressed) {
                direction = "left";
            }
            else {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objectIndex = gp.cChecker.checkObject(this, true);

            // If there is no collision based on the key pressed, then the player can move in that direction
            if(!collisionOn) {
                switch(direction) {
                    case "up" :
                        worldY = Math.max(worldY - speed, 0);
                        break;
                    case "down" :
                        worldY = Math.min(worldY + speed, (gp.maxWorldRow - 1) * gp.tileSize); // the "-1" is added so the character does not cross the border
                        break;
                    case "left" :
                        worldX = Math.max(worldX - speed, 0);
                        break;
                    case "right" :
                        worldX = Math.min(worldX + speed, (gp.maxWorldCol - 1) * gp.tileSize);
                        break;
                }
            }

            spriteCounter++;
        }

        // Change the sprite once every 10 frames; 6 changes a second (60 FPS)
        if(spriteCounter > 10) {
            if(spriteNum == 1) spriteNum = 2;
            else spriteNum = 1;

            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;

        switch (direction) {
           case "up":
               if(spriteNum == 1){
                   image = up1;
               } else {
                   image = up2;
               }
               break;
           case "down":
               if(spriteNum == 1){
                   image = down1;
               } else {
                   image = down2;
               }
               break;
           case "left":
               if(spriteNum == 1){
                   image = left1;
               } else  {
                   image = left2;
               }
               break;
           case  "right":
               if(spriteNum == 1){
                   image = right1;
               } else {
                   image = right2;
               }
               break;
        };

        g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
