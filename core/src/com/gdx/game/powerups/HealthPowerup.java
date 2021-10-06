package com.gdx.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.Collidable;
import com.gdx.game.GameObject;
import com.gdx.game.moveable.Tank;
import com.gdx.loaders.Observable;

public class HealthPowerup extends Powerup implements Collidable {
    public HealthPowerup(float locationInX, float locationInY, float width, float height, Texture texture) {
        super(locationInX, locationInY, width, height, texture);
    }

    @Override
    public void execute(Tank tank) {
        tank.setHealth(tank.MAX_HEALTH);

        this.setUpdating(false);
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        super.draw(batch, shapeRenderer);
    }

    @Override
    public void update(Observable obv) {

    }

    @Override
    public void render(Observable obv) {
        super.render(obv);
    }

    @Override
    public boolean checkCollision(GameObject object) {
        boolean collided = this.getHitbox().overlaps(object.getHitbox());
        if (object instanceof Tank  && collided && isUpdating()){
            this.execute((Tank) object);
        }
        return collided;
    }
}
