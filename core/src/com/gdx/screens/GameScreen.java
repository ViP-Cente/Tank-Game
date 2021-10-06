package com.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.*;
import com.gdx.game.Collidable;
import com.gdx.game.Controller;
import com.gdx.game.powerups.Powerup;
import com.gdx.loaders.Resources;
import com.gdx.game.moveable.Tank;
import com.gdx.launchers.TankMain;
import com.gdx.map.MapHandler;

public class GameScreen extends ScreenAdapter {

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapHandler mapHandler;
    private OrthographicCamera camera1;
    private OrthographicCamera camera2;
    private OrthographicCamera minimapCamera;

    private Viewport playerOneViewport;
    private Viewport playerTwoViewport;
    private Viewport minimapViewport;

    private Tank tank1;
    private Tank tank2;
    private TankMain game;


    public GameScreen(TankMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        //loading the Tiled map for the game
        game.resetMap();
        mapHandler = game.getMapHandler();
        map = mapHandler.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/30f); //loading the renderer we will use for the game


        //Creating two cameras with their respective viewports
        camera1 = new OrthographicCamera();
        camera2 = new OrthographicCamera();
        minimapCamera = new OrthographicCamera();


        playerOneViewport = new ExtendViewport(30, 30,camera1);
        playerTwoViewport = new ExtendViewport(30, 30, camera2);
        minimapViewport = new ExtendViewport(mapHandler.mapWidthInTiles * 2, mapHandler.mapHeightInTiles * 2,minimapCamera);

        //Creating two player tanks
        tank1 = new Tank(mapHandler.mapWidthInTiles/2, 10, 2,2,0,Resources.getTexture("tankTexture1"),
                new Controller(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.F));
        tank2 = new Tank(mapHandler.mapWidthInTiles/2, mapHandler.mapHeightInTiles - 10, 2,2,0,Resources.getTexture("tankTexture1"),
                new Controller(Input.Keys.UP, Input.Keys.DOWN , Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER));
        //Attaching observers and collidables
        game.attachObserver(tank1);
        game.attachObserver(tank2);
        game.attachCollidable(tank1);
        game.attachCollidable(tank2);
        for (Powerup powerup: mapHandler.getPowerups()) {
            game.attachObserver(powerup);
            game.attachCollidable((Collidable) powerup);
        }
    }

    @Override
    public void render(float delta) {
        //Clear the screen each time we render
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //Render observers
        game.notifyObservers();
        game.renderObservers();

        //These two are updated in their own because we can't add bullets while we are updating the observers
        //So we add the bullets to the observerList if the tank is shooting
        checkShoot(tank1);
        checkShoot(tank2);
        //Checks the collision for the tanks and it's bullets
        game.checkCollision(tank1);
        game.checkCollision(tank2);


        checkLives();

        mapRenderer.setView(camera1);   //Set the renderer to render for screen 1
        renderViewport(playerOneViewport, camera1, tank1);  //Render the screen for player 1

        mapRenderer.setView(camera2);   //Set the renderer to render for screen 2
        renderViewport(playerTwoViewport,camera2, tank2);   //Render the screen for player 2

        mapRenderer.setView(minimapCamera);
        minimapViewport.apply();
        mapRenderer.render();

        mapRenderer.getBatch().begin();
        game.drawObservers(mapRenderer.getBatch(), game.getShapeRenderer());
        mapRenderer.getBatch().end();

        minimapCamera.update();

    }

    @Override
    public void hide() {
        game.detachObserver(tank1);
        game.detachObserver(tank2);
        game.detachCollidable(tank1);
        game.detachCollidable(tank2);

    }

    //This method resizes the screen so we have a split screen setup
    @Override
    public void resize(int width, int height){
        playerOneViewport.update(width/2-5,height);
        playerTwoViewport.update(width/2-5,height);
        playerTwoViewport.setScreenX(width / 2);
        minimapViewport.update(width/2, height/2, true);
        minimapViewport.setScreenPosition(width/2 - minimapViewport.getScreenWidth()/4,height - minimapViewport.getScreenHeight()/2);
        camera1.update();
        camera2.update();
        minimapCamera.update();
    }


    private void renderViewport(Viewport viewport, OrthographicCamera camera, Tank tank){
        viewport.apply();
        mapRenderer.render();
        camera.position.set(tank.getPosition().x,tank.getPosition().y,0);

        mapRenderer.getBatch().begin();
        game.drawObservers(mapRenderer.getBatch(), game.getShapeRenderer());
        mapRenderer.getBatch().end();

        camera.update();
    }

    private void checkLives(){
        if(!tank1.isUpdating() || !tank2.isUpdating()){
            game.setScreen(new EndScreen(game));
        }
    }

    private void checkShoot(Tank tank){
        if (tank.isBulletShot()){
            tank.shoot(game);
            tank.setBulletShot(false);
        }
    }




}
