package main;

import entity.Entity;
import object.SuperObject;

import java.awt.*;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height; // TODO: Add bottom padding

        // find the column and row numbers of each of the above coordinates in the world map
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // based on the action, check if there is a possibility for collision
        switch(entity.direction) {
            case "up" :
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 =  gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
                if(gp.tileManager.tile[tileNum1].collision ||  gp.tileManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }
                break;
            case "down" :
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
                tileNum2 =  gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
                if(gp.tileManager.tile[tileNum1].collision ||  gp.tileManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }
                break;
            case "left" :
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 =  gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
                if(gp.tileManager.tile[tileNum1].collision ||  gp.tileManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }
                break;
            case "right" :
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
                tileNum2 =  gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
                if(gp.tileManager.tile[tileNum1].collision ||  gp.tileManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        // track the index of the object the player is colliding with
        int index = 999;

        for(int i=0; i<gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // object's solid area position; the latter term is added in case a padding is added in the future
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction) {
                    case "up" :
                        entity.solidArea.y -= entity.speed;
                        if(gp.obj[i].solidArea.intersects(entity.solidArea) && gp.obj[i].collision) entity.collisionOn = true; // this ensures the collision set by checkTiles @ Player class is not reset to false if there is no intersection with an object
                        if(player) index = i;
                        break;
                    case "down" :
                        entity.solidArea.y += entity.speed;
                        if(gp.obj[i].solidArea.intersects(entity.solidArea) && gp.obj[i].collision) entity.collisionOn = true;
                        if(player) index = i;
                        break;
                    case "left" :
                        entity.solidArea.x -= entity.speed;
                        if(gp.obj[i].solidArea.intersects(entity.solidArea) && gp.obj[i].collision) entity.collisionOn = true;
                        if(player) index = i;
                        break;
                    case "right" :
                        entity.solidArea.x += entity.speed;
                        if(gp.obj[i].solidArea.intersects(entity.solidArea) && gp.obj[i].collision) entity.collisionOn = true;
                        if(player) index = i;
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;

                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }

        }

        return index;
    }
}
