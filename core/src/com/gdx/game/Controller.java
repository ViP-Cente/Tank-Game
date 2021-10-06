package com.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Controller {
    private int up, down, left, right, shoot;

    public Controller(int up, int down, int left, int right, int shoot) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
    }

    public boolean isUpPressed() {
        return Gdx.input.isKeyPressed(up);
    }

    public boolean isDownPressed() {
        return Gdx.input.isKeyPressed(down);
    }

    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(left);
    }

    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(right);
    }

    public boolean isShootPressed() {
        return Gdx.input.isKeyPressed(shoot);
    }
}

