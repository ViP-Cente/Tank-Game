package com.gdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.map.MapHandler;
import com.gdx.screens.GameScreen;

public abstract class GameObject implements Observer {
    private Vector2 position;
    private Rectangle hitbox;
    private Sprite sprite;
    private Texture texture;
    private boolean isUpdating = true;




    public GameObject (float locationInX, float locationInY, float width, float height, Texture texture){
        // Set the positions and create the hitbox
        this.position = new Vector2(locationInX,locationInY);
        this.hitbox = new Rectangle(locationInX, locationInY, width , height );
        //Create the sprite
        this.texture = texture;
        this.sprite = new Sprite(texture);
        //Set the position and bounds for the sprite
        this.sprite.setPosition(this.getPosition().x,this.getPosition().y);
        this.sprite.setBounds(this.getHitbox().x,this.getHitbox().y,width,height );
        //Set the origin at the center of the sprite's position. Used to rotate the tank around its own center
        this.sprite.setOriginCenter();
    }

    public abstract void draw(Batch batch, ShapeRenderer shapeRenderer);


    public Vector2 getPosition(){ return position; }


    public void setPosition(float x, float y){ position.set(x,y); }

    public Rectangle getHitbox(){ return hitbox; }

    public void setHitbox(float locationInX, float locationInY, float width, float height){
        this.hitbox.set(locationInX  , locationInY  , width   ,  height);
    }
    public float getPositionInX(){
        return position.x;
    }
    public float getPositionInY(){
        return position.y;
    }

    public Sprite getSprite(){
        return this.sprite;
    }


    public void dispose(){
        this.texture.dispose();
    }

    //FOR DEBUGGING: This method draws the hitbox
    public void drawHitbox(Batch batch, ShapeRenderer shapeRenderer){
        batch.end();    //End the current batch so we don't have two current batches openn at once

        //Set the shapeRenderer to the batch's projection and transformation matrix
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        //Set the color of the hitbox to purple
        shapeRenderer.setColor(Color.PURPLE);
        //begin drawing the hitbox
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(this.getHitbox().getX(),this.getHitbox().getY(),this.getHitbox().getWidth(),this.getHitbox().getHeight());
        shapeRenderer.end();

        batch.begin();  //Start the batch again
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public void setUpdating(boolean updating) {
        isUpdating = updating;
    }


    //This method will stop all the actions of the Game object like drawing, rendering. ect.
    public void stop(){
        this.setUpdating(false);
    }



    /* public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.sprite.setPosition(this.getPosition().x,this.getPosition().y);
        this.sprite.setBounds(this.getHitbox().x,this.getHitbox().y,this.getHitbox().width,this.getHitbox().height);
        this.sprite.setOriginCenter();
    }
    */
}
