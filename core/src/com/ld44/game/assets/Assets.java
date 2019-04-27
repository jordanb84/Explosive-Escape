package com.ld44.game.assets;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ld44.game.animation.Frame;

import java.util.ArrayList;
import java.util.List;

public class Assets {

    private static final Assets INSTANCE = new Assets();

    private AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();

        this.loadAssets();
    }

    private void loadAssets() {
        this.loadTextures("entity/player.png", "entity/player_small.png");
        this.loadTextures("entity/small_0.png", "entity/small_1.png", "entity/small_2.png", "entity/small_3.png", "entity/small_4.png");
        this.loadTexture("entity/enemy_small.png");
        this.loadTexture("entity/basicBullet.png");
        this.loadTexture("radar.png");
        this.loadTexture("tile/water5.png");

        this.loadDirectory("explosion/small");
        this.loadDirectory("crosshair");

        this.loadTexture("ui/bar.png");

        this.loadTextures("tile/air.png", "tile/grass.png", "tile/stone.png", "tile/water.png");

        this.loadTexture("editorBackground.png");
        this.loadTexture("entity/test.png");

        this.loadTextures("entity/water_ripple_small_000.png", "entity/water_ripple_small_001.png", "entity/water_ripple_small_002.png", "entity/water_ripple_small_003.png", "entity/water_ripple_small_004.png");

        this.assetManager.load("skin/holo/Holo-dark-hdpi.json", Skin.class);

        this.assetManager.load("skin/arcade/arcade-ui.json", Skin.class);


        this.assetManager.finishLoading();
    }

    public void loadDirectory(String path) {
        for(FileHandle file : Gdx.files.internal(path).list()) {
            this.loadTexture(path + "/" + file.name());
            System.out.println("Loaded " + path + "/" + file.name());
        }
    }

    public List<Sprite> getDirectory(String path) {
        List<Sprite> sprites = new ArrayList<Sprite>();

        for(FileHandle file : Gdx.files.internal(path).list()) {
            sprites.add(this.getSprite(path + "/" + file.name()));
        }

        return sprites;
    }

    public List<String> getDirectoryPaths(String path) {
        List<String> sprites = new ArrayList<String>();

        for(FileHandle file : Gdx.files.internal(path).list()) {
            sprites.add(path + "/" + file.name());
        }

        return sprites;
    }

    public void loadTexture(String path) {
        this.assetManager.load(path, Texture.class);
    }

    public void loadTextures(String ... paths) {
        for(String path : paths) {
            this.assetManager.load(path, Texture.class);
        }
    }

    public Texture getTexture(String name) {
        return this.assetManager.get(name, Texture.class);
    }

    public Sprite getSprite(String name) {
        return new Sprite(this.getTexture(name));
    }

    public void loadSkin(String path) {
        this.assetManager.load(path, Skin.class);
    }

    public Skin getSkin(String path) {
        return this.assetManager.get(path, Skin.class);
    }

    public void loadMusic(String path) {
        this.assetManager.load(path, Music.class);
    }

    public Music getMusic(String path) {
        return this.assetManager.get(path, Music.class);
    }

    public void loadSoundEffect(String path) {
        this.assetManager.load(path, Sound.class);
    }

    public Sound getSoundEffect(String path) {
        return this.assetManager.get(path, Sound.class);
    }


    public static Assets getInstance() {
        return INSTANCE;
    }

}
