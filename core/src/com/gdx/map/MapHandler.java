package com.gdx.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.gdx.game.GameObject;
import com.gdx.game.moveable.Bullet;
import com.gdx.game.powerups.BulletPowerup;
import com.gdx.game.powerups.HealthPowerup;
import com.gdx.game.powerups.Powerup;
import com.gdx.game.powerups.ShieldPowerup;
import com.gdx.loaders.Observable;
import com.gdx.loaders.Resources;

import java.util.ArrayList;

public class MapHandler {
    private TiledMap map;
    private final AssetManager manager;
    private TiledMapTileLayer  ground, breakableWall, unbreakableWall;
    private  ArrayList<Powerup> powerups;
    public int tileWidth, tileHeight,
            mapWidthInTiles, mapHeightInTiles,
            mapWidthInPixels, mapHeightInPixels;

    public MapHandler(String filename){
        powerups = new ArrayList<>();

        manager = new AssetManager();

        ground = new TiledMapTileLayer(0,0,0,0);
        breakableWall = new TiledMapTileLayer(0,0,0,0);

        loadMap(filename);
        loadMapProperties();
        loadLayers();
        loadObjects();
    }

    public TiledMap getMap(){
        return map;
    }

    public TiledMapTileLayer getBreakableWall() {
        return breakableWall;
    }

    public TiledMapTileLayer getUnbreakableWall() {
        return unbreakableWall;
    }

    public ArrayList<Powerup> getPowerups() {
        return powerups;
    }

    private void loadMap(String filename){
        manager.setLoader(TiledMap.class,new TmxMapLoader());
        manager.load(filename, TiledMap.class);
        manager.finishLoading();
        map = manager.get(filename, TiledMap.class);
    }

    private void loadMapProperties(){
        MapProperties properties = map.getProperties();
        tileWidth         = properties.get("tilewidth", Integer.class);
        tileHeight        = properties.get("tileheight", Integer.class);
        mapWidthInTiles   = properties.get("width", Integer.class);
        mapHeightInTiles  = properties.get("height", Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
    }

    private void loadLayers(){
        ground = (TiledMapTileLayer) map.getLayers().get("Ground");
        breakableWall = (TiledMapTileLayer) map.getLayers().get("BreakableWall");
        unbreakableWall = (TiledMapTileLayer) map.getLayers().get("UnbreakableWall");
    }

    private void loadObjects(){
        for (MapObject object : map.getLayers().get("HealthPowerup").getObjects()){
            if (object instanceof TextureMapObject) {
                powerups.add(new HealthPowerup(((TextureMapObject) object).getX()/tileWidth, ((TextureMapObject) object).getY()/tileHeight,
                        (((TextureMapObject) object).getTextureRegion().getRegionWidth() * ((TextureMapObject) object).getScaleX())/tileWidth,
                        (((TextureMapObject) object).getTextureRegion().getRegionHeight() * ((TextureMapObject) object).getScaleY())/tileHeight,
                        Resources.getTexture("healthPowerup")));
            }
        }
        for (MapObject object : map.getLayers().get("ShieldPowerup").getObjects()){
            if (object instanceof TextureMapObject) {
                powerups.add(new ShieldPowerup(((TextureMapObject) object).getX()/tileWidth, ((TextureMapObject) object).getY()/tileHeight,
                        (((TextureMapObject) object).getTextureRegion().getRegionWidth() * ((TextureMapObject) object).getScaleX())/tileWidth,
                        (((TextureMapObject) object).getTextureRegion().getRegionHeight() * ((TextureMapObject) object).getScaleY())/tileHeight,
                        Resources.getTexture("shieldPowerup")));
            }
        }
        for (MapObject object : map.getLayers().get("BulletPowerup").getObjects()){
            if (object instanceof TextureMapObject) {
                powerups.add(new BulletPowerup(((TextureMapObject) object).getX()/tileWidth, ((TextureMapObject) object).getY()/tileHeight,
                        (((TextureMapObject) object).getTextureRegion().getRegionWidth() * ((TextureMapObject) object).getScaleX())/tileWidth,
                        (((TextureMapObject) object).getTextureRegion().getRegionHeight() * ((TextureMapObject) object).getScaleY())/tileHeight,
                        Resources.getTexture("bulletPowerup")));
            }
        }
    }

    //This method checks the collisions for the walls in the game
    public boolean checkWalls(GameObject object){
        if (getBreakableWall().getCell((int) object.getPositionInX(), (int) object.getPositionInY()) != null) {
            if (object instanceof Bullet && object.isUpdating()){
                System.out.println("wall collided");
                getBreakableWall().setCell((int) object.getPositionInX(), (int) object.getPositionInY(),null);
                object.stop();
            }
            return true;
        }
        if (getUnbreakableWall().getCell((int) object.getPositionInX(), (int) object.getPositionInY()) != null) {
            if (object instanceof Bullet && object.isUpdating()){
                System.out.println("wall collided");

                object.stop();
            }
            return true;
        }
        return false;
    }


}
