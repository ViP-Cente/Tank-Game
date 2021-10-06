package com.gdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.loaders.Observable;

public interface Observer {
    public void update(Observable obv);
    public void render(Observable obv);
    public void draw(Batch batch, ShapeRenderer shapeRenderer);

}
