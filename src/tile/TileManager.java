package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; // Supports 10 different types of tiles
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("/res/maps/world01.txt");
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/grass.png")));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/wall.png")));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/water.png")));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/earth.png")));

            tile[4] = new Tile();
            tile[4].image  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/tree.png")));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/sand.png")));
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(filePath));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;

            while(row < gp.maxWorldRow) {
                String line = br.readLine();

                String[] numbers = line.split(" ");
                int col = 0;

                while(col < gp.maxWorldCol) {
                    mapTileNum[row][col] = Integer.parseInt(numbers[col]);
                    col++;
                }

                row++;
            }
        } catch(Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        int worldCol = 0;
        int worldRow = 0;

        while(worldRow < gp.maxWorldRow && worldCol < gp.maxWorldCol) {
            int tileNum = mapTileNum[worldRow][worldCol];
            int tileWorldX = gp.tileSize * worldCol;
            int tileWorldY = gp.tileSize * worldRow;

            // find the offset of the tile from the player in the world, then translate the tile by that offset from the character's on-screen position (center)
            int tileScreenX = tileWorldX - gp.player.worldX + gp.player.screenX;
            int tileScreenY = tileWorldY - gp.player.worldY + gp.player.screenY;

            if(tileScreenX > -1 * gp.tileSize && tileScreenY > -1 * gp.tileSize && tileScreenX < gp.screenWidth && tileScreenY < gp.screenHeight) {
                g2d.drawImage(tile[tileNum].image, tileScreenX, tileScreenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldRow++;
                worldCol = 0;
            }
        }
    }
}
