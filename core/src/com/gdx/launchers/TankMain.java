package com.gdx.launchers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.Collidable;
import com.gdx.game.GameObject;
import com.gdx.game.Observer;
import com.gdx.loaders.Observable;
import com.gdx.map.MapHandler;
import com.gdx.screens.TitleScreen;

import java.util.*;


public class TankMain extends Game implements Observable {
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private List<Observer> observers;
    private List<Collidable> collidables;
    private MapHandler mapHandler;
    ///I'm using this function to load all the assets into the game before we launch it
    @Override
    public void create(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        observers = Collections.synchronizedList(new ArrayList<Observer>());
        mapHandler = new MapHandler("finalTankMap.tmx");
        collidables = Collections.synchronizedList(new ArrayList<Collidable>());

        //Sets the first screen to the titleScreen
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose(){
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
        observers.clear();
    }


    @Override
    public void notifyObservers() {
        for (Observer observer : this.observers) {
            observer.update(this);
        }
    }

    @Override
    public void renderObservers() {for (Observer observer : this.observers){
            observer.render(this);
        }
    }

    @Override
    public void attachObserver(Observer object) {
        this.observers.add(object);
    }

    @Override
    public void detachObserver(Observer object) {
        this.observers.remove(this.observers.remove(object));
    }

    @Override
    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public void drawObservers(Batch batch, ShapeRenderer shapeRenderer){
        for (Observer observer : this.observers){
            observer.draw(batch, shapeRenderer);
        }
    }


@Override
    public void attachCollidable(Collidable collidable){
        this.collidables.add(collidable);
    }
@Override
    public void detachCollidable(Collidable collidable){
        this.collidables.remove(collidable);
    }

    @Override
    public void checkCollision(GameObject object){
        for (Collidable collidable: this.collidables) {
            collidable.checkCollision(object);
        }
    }

    public void resetMap(){
        mapHandler = new MapHandler("finalTankMap.tmx");
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public BitmapFont getFont() {
        return font;
    }

    public SpriteBatch getBatch() {
        return batch;
    }


}
