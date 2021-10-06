package com.gdx.loaders;

import com.gdx.game.Collidable;
import com.gdx.game.GameObject;
import com.gdx.game.Observer;
import com.gdx.map.MapHandler;

public interface Observable {


    void notifyObservers();

    void renderObservers();

    void attachObserver(Observer object);

    void detachObserver(Observer object);

    MapHandler getMapHandler();

    void checkCollision(GameObject object);
    void attachCollidable(Collidable collidable);
    void detachCollidable(Collidable collidable);
}
