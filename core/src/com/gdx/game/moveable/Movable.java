package com.gdx.game.moveable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.GameObject;
import com.gdx.loaders.Observable;
import com.gdx.map.MapHandler;

public abstract class Movable extends GameObject {
    private Vector2 velocity;
    private float currentAngle;

    private final float ROTATION_SPEED = 3f;

    public Movable(float locationInX, float locationInY, float width, float height, float currentAngle, Texture texture) {
        super(locationInX, locationInY, width, height, texture);
        this.velocity = new Vector2(0,0);
        this.currentAngle = currentAngle;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public void setVelocity(float velocityInX, float velocityInY){
        this.velocity.set(velocityInX,velocityInY);
    }

    public float getVelocityInX() {
        return velocity.x;
    }

    public float getVelocityInY() {
        return velocity.y;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void moveBackwards(float speed) {
        setVelocity((float) ( speed * Math.cos(Math.toRadians(getCurrentAngle()))),
                (float) ( speed * Math.sin(Math.toRadians(getCurrentAngle()))));
        setPosition(getPosition().x - getVelocityInX(), getPosition().y - getVelocityInY());
    }

    public void moveForwards(float speed) {
        setVelocity((float) ( speed * Math.cos(Math.toRadians(getCurrentAngle()))),
                (float) ( speed * Math.sin(Math.toRadians(getCurrentAngle()))));
        setPosition(getPosition().x + getVelocityInX(), getPosition().y + getVelocityInY());
    }

    public void rotateRight() {
        setCurrentAngle(getCurrentAngle() + ROTATION_SPEED);
    }

    public void rotateLeft() {
        setCurrentAngle(getCurrentAngle() - ROTATION_SPEED);
    }

    public boolean checkBorder(GameObject object, Observable observable){
        return object.getPositionInX() < 0 || object.getPositionInX() > observable.getMapHandler().mapWidthInTiles - 2 || object.getPositionInY() < 0
                || object.getPositionInY() > observable.getMapHandler().mapHeightInTiles - 2;
    }

}
