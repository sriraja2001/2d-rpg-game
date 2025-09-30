package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 pixels - The size of a single tile in the game
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 pixels; The scaling can be adjusted as needed

    // ASPECT RATIO: 4:3
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
    public final int screenHeight = maxScreenRow * tileSize; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;

    // GAME FPS
    final int fps = 60;

    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();
    TileManager tileManager = new TileManager(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player =  new Player(this, keyHandler);
    public SuperObject[] obj = new SuperObject[10];

    // STATS
    double currentFPS;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

    }

    public void setupGame() {
        aSetter.setObject();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // LAYERS
        // first draw the tiles and then draw the player, else the tile hides the player

        // TILES
        tileManager.draw(g2d);

        // OBJECTS
        for(SuperObject obj : obj){
            if(obj != null) obj.draw(g2d, this);
        }

        // PLAYER
        player.draw(g2d);

        // display the real-time FPS on the screen
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString(currentFPS + " FPS", screenWidth - 75, 25);

        g2d.dispose();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / fps;
        double lastTime = System.nanoTime();
        double currentTime;
        double delta = 0;

        // CALCULATE FPS
        double updateCount = 0;
        double timer = 0;

        while(gameThread.isAlive()){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                updateCount++;
            }

            if(timer >= 1000000000) {
//                System.out.println("FPS: " + (updateCount));
                currentFPS = updateCount;
                updateCount = 0;
                timer = 0;
            }
        }
    }
}
