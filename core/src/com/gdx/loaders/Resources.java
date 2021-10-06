package com.gdx.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    //HashMaps of textures that we will be using for the game
    private AssetManager assetManager;
    private static Map<String, Texture> textures;


    public Resources() {
        assetManager = new AssetManager();
    }

    static {
        textures = new HashMap<>();

        textures.put("titleScreen", new Texture(Gdx.files.internal("title.png")));
        textures.put("tankTexture1", new Texture(Gdx.files.internal("tank1.png")));
        textures.put("bulletTexture", new Texture(Gdx.files.internal("bullet.png")));
        textures.put("healthPowerup", new Texture(Gdx.files.internal("Bouncing.gif")));
        textures.put("bulletPowerup", new Texture(Gdx.files.internal("Weapon.gif")));
        textures.put("shieldPowerup", new Texture(Gdx.files.internal("Pickup.gif")));


    }

    public AssetManager getAssetManager(){
        return this.assetManager;
    }

    public static Texture getTexture(String key){
        return textures.get(key);
    }

}
