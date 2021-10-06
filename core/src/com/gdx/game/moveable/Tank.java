package com.gdx.game.moveable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.gdx.game.Collidable;
import com.gdx.game.Controller;
import com.gdx.game.GameObject;
import com.gdx.game.powerups.Powerup;
import com.gdx.loaders.Observable;
import com.gdx.loaders.Resources;

import java.util.ArrayList;

public class Tank extends Movable implements Collidable {
    private final float SPEED = 0.25f;
    private final Controller controller;



    private boolean bulletShot = false;
    private final ArrayList<Bullet> bullets;
    private int shootCooldown = 0;

    private int damage = 5;
    private int damageCooldown = 0;

    private final Rectangle shield;
    private int shieldCooldown = 0;

    public final float MAX_HEALTH = 100;
    private float health = MAX_HEALTH;
    private final float healthBarWidth = 10;
    private final Rectangle healthBar;

    private int lives = 3;


    public Tank(float x, float y, float width, float height, float angle, Texture texture, Controller controller){
        super(x,y,width, height, angle, texture);
        this.controller = controller;
        bullets = new ArrayList<>();
        healthBar = new Rectangle(this.getHitbox().x,this.getHitbox().y + 2,healthBarWidth,this.getHitbox().height/2);
        shield = new Rectangle(this.getHitbox());

    }

    public boolean isBulletShot() {
        return bulletShot;
    }

    public void setBulletShot(boolean bulletShot) {
        this.bulletShot = bulletShot;
    }

    public void setDamage(int damage) {
        damageCooldown = 1000;
        this.damage = damage;
    }

    private void renderDamage(){
        if (damageCooldown != 0){
            damageCooldown -= 1;
            this.getSprite().setColor(Color.GOLDENROD);
        }
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void activateShield(){
        shieldCooldown = 1000;
    }

    private void renderShield(){
        if (shieldCooldown != 0){
            this.shield.set(this.getPositionInX()-0.5f,this.getPositionInY()-0.5f,this.getHitbox().getWidth()+1, this.getHitbox().getHeight()+1);
            shieldCooldown -= 1;
        }
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        if(shieldCooldown != 0){
            drawShield(batch, shapeRenderer);
        }
        this.getSprite().draw(batch);

        // DEBUGGING: Draws the hitboxes for the tank
        this.drawHitbox(batch, shapeRenderer);
        this.drawHealthbar(batch, shapeRenderer);
    }

    @Override
    public void render(Observable observable){
        this.getSprite().setRotation(this.getCurrentAngle());
        this.getSprite().setPosition(this.getPosition().x, this.getPosition().y);

        this.setHitbox(this.getPositionInX(), this.getPositionInY(),this.getHitbox().getWidth(),this.getHitbox().getHeight());
        this.healthBar.set(this.getPositionInX(), this.getPositionInY() +2, healthBarWidth * (health/100) , healthBar.height);
        if (this.health == 100){
            this.getSprite().setColor(Color.RED);
        }
        else {
            this.getSprite().setColor(Color.FOREST);
        }
        renderShield();
        renderDamage();
    }

    @Override
    public void update(Observable observable) {

        if (this.controller.isUpPressed()) {
            this.moveForwards(SPEED);
            if (observable.getMapHandler().checkWalls( this) || checkBorder(this, observable)) {
                System.out.println("wall collided");
                this.moveBackwards(SPEED);
            }
        }
        if (this.controller.isDownPressed()) {
            this.moveBackwards(SPEED);
            if (observable.getMapHandler().checkWalls( this) || checkBorder(this, observable)) {
                System.out.println("wall collided");
                this.moveForwards(SPEED);
            }
        }
        if (this.controller.isLeftPressed()) {
            this.rotateLeft();
        }
        if (this.controller.isRightPressed()) {
            this.rotateRight();
        }
        if (this.controller.isShootPressed()){
            if (shootCooldown == 0) {
                bulletShot = true;
                shootCooldown = 15;
            }
            else {
                shootCooldown -= 1;
            }
        }

        //If all lives are lost, stop updating the tank
        if (lives < 0){
            this.setUpdating(false);
        }
    }

    @Override
    public boolean checkCollision(GameObject object) {
        //Checks if the bullet hits the shield if present
        if (this.shield.overlaps(object.getHitbox()) && object instanceof Bullet && shieldCooldown != 0 && !bullets.contains(object)){
            object.stop();
        }

        //Checks if a bullet hits the tank and if it is an enemy bullet that is being updated
        boolean collided = this.getHitbox().overlaps(object.getHitbox());
        if (object instanceof Bullet && collided && !bullets.contains(object) && object.isUpdating() ){
                System.out.println("Bullet collided");
                //Subtracts the health by the bullet damage
                this.health -= ((Bullet) object).getDamage();
                //Reduce the lives if all health is lost
                if(this.health < 0){
                    this.health = MAX_HEALTH;
                    this.lives -= 1;
                }
                System.out.println(health);
                object.stop();
        }
        return collided;
    }




    private void drawHealthbar(Batch batch, ShapeRenderer shapeRenderer){
        batch.end();    //End the current batch so we don't have two current batches open at once
        //Set the shapeRenderer to the batch's projection and transformation matrix
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        //Set the color of the healthbar to purple
        shapeRenderer.setColor(Color.RED);
        //begin drawing the healthbar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(this.healthBar.getX(),this.healthBar.getY(),this.healthBar.getWidth(),this.healthBar.getHeight());
        shapeRenderer.end();

        batch.begin();
    }

    public void shoot(Observable observable){
        Bullet bullet = new Bullet(this.getPositionInX(),this.getPositionInY(),1f,1f,this.getCurrentAngle(), Resources.getTexture("bulletTexture"));
        //Upgrades the damage if the bullet powerup is required
        if (damageCooldown != 0){
            bullet.setDamage(10);
        }
        bullets.add(bullet);
        observable.attachObserver(bullet);
        observable.attachCollidable(bullet);
    }



    private void drawShield(Batch batch, ShapeRenderer shapeRenderer){
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.BLUE);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(this.shield.getX(),this.shield.getY(),this.shield.getWidth(),this.shield.getHeight());
        shapeRenderer.end();

        batch.begin();
    }


}
