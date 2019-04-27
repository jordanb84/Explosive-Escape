package com.ld44.game.assets;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    private static final Assets INSTANCE = new Assets();

    private AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();

        this.loadAssets();
    }

    private void loadAssets() {
        this.loadTexture("entity/player.png");
        this.loadTexture("entity/basicBullet.png");

        this.loadTextures("tile/air.png", "tile/grass.png", "tile/stone.png", "tile/water.png");

        this.loadTexture("editorBackground.png");

        this.assetManager.load("skin/holo/Holo-dark-hdpi.json", Skin.class);

        this.assetManager.load("skin/arcade/arcade-ui.json", Skin.class);

        this.assetManager.finishLoading();
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
