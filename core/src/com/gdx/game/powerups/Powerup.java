package com.gdx.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.GameObject;
import com.gdx.game.moveable.Tank;
import com.gdx.loaders.Observable;

public abstract class Powerup extends GameObject  {
    private int cooldown = 30;
    public Powerup(float locationInX, float locationInY, float width, float height, Texture texture) {
        super(locationInX, locationInY, width, height, texture);
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer){
        if (isUpdating()) {
            this.getSprite().draw(batch);
            this.drawHitbox(batch, shapeRenderer);
        }
    }

    @Override
    public void render(Observable observable){
        if (isUpdating()) {
            this.getSprite().setPosition(this.getPosition().x, this.getPosition().y);
            this.setHitbox(this.getPositionInX(), this.getPositionInY(), this.getHitbox().getWidth(), this.getHitbox().getHeight());
        } else {
            cooldown -= 1;
            if (cooldown == 0){
                this.setUpdating(true);
                cooldown = 2000;
            }
        }
    }

    public abstract void execute(Tank tank);
}
