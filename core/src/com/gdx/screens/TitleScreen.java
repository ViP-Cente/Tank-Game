package com.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.gdx.loaders.Resources;
import com.gdx.launchers.TankMain;

public class TitleScreen extends ScreenAdapter {
    TankMain game;

    public TitleScreen(TankMain game){
        this.game = game;
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    game.setScreen(new GameScreen(game));
                }
                return true;
            }
        });
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        game.getBatch().draw(Resources.getTexture("titleScreen"), 0, 0);
        game.getFont().draw(game.getBatch(), "Press space to start.", game.getMapHandler().mapWidthInTiles * .25f, game.getMapHandler().mapHeightInTiles * .25f);
        game.getBatch().end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
