package com.gdx.game.moveable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.Collidable;
import com.gdx.game.GameObject;
import com.gdx.loaders.Observable;
import com.gdx.map.MapHandler;

import java.util.Map;

public class Bullet extends Movable implements Collidable {
    private int damage;
    private final float SPEED = 0.75f;
    public Bullet(float locationInX, float locationInY, float width, float height, float angle, Texture texture) {
        super(locationInX, locationInY, width, height, angle, texture);
        this.moveForwards(SPEED);
        damage = 5;
    }


    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        if (isUpdating()) {
            this.getSprite().draw(batch);
            this.drawHitbox(batch, shapeRenderer);
        }
    }

    @Override
    public void update(Observable observable) {
        if (isUpdating()) {
            this.moveForwards(SPEED);
        }
        if (checkBorder(this, observable)){
            this.stop();
        }
        observable.getMapHandler().checkWalls(this);
    }

    @Override
    public void render(Observable obv) {
        if (isUpdating()) {
            this.getSprite().setRotation(this.getCurrentAngle());
            this.getSprite().setPosition(this.getPosition().x, this.getPosition().y);
            this.setHitbox(this.getPositionInX(), this.getPositionInY(), this.getHitbox().getWidth(), this.getHitbox().getHeight());
        }
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    //I check the collisions for the bullet in Tank class
    @Override
    public boolean checkCollision(GameObject object) {
        boolean collided = this.getHitbox().overlaps(object.getHitbox());
       if (object instanceof Tank && collided){
           ((Tank) object).checkCollision(this);
       }
       return collided;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


}
