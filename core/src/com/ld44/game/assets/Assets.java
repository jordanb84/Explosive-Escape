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

        //this.loadAssets();
    }

    public void loadAssets() {
        this.loadTextures("entity/player.png", "entity/player_small.png");
        this.loadTextures("entity/small_0.png", "entity/small_1.png", "entity/small_2.png", "entity/small_3.png", "entity/small_4.png");
        this.loadTextures("entity/small_0b.png", "entity/small_1b.png", "entity/small_2b.png", "entity/small_3b.png", "entity/small_4b.png");
        this.loadTextures("entity/small_double_0.png", "entity/small_double_1.png", "entity/small_double_2.png", "entity/small_double_3.png", "entity/small_double_4.png");

        this.loadTextures("entity/medium_double_0.png", "entity/medium_double_1.png", "entity/medium_double_2.png", "entity/medium_double_3.png", "entity/medium_double_4.png");
        this.loadTextures("entity/medium_large_0.png", "entity/medium_large_1.png", "entity/medium_large_2.png", "entity/medium_large_3.png", "entity/medium_large_4.png");
        this.loadTextures("ui/medium_double_side.png", "ui/medium_double_side_down.png", "ui/medium_double_side_hover.png");
        this.loadTextures("ui/medium_double_side_locked.png", "ui/medium_double_side_down_locked.png", "ui/medium_double_side_hover_locked.png");

        this.loadTextures("entity/large_gunned_0.png", "entity/large_gunned_1.png", "entity/large_gunned_2.png", "entity/large_gunned_3.png", "entity/large_gunned_4.png");

        this.loadTexture("entity/enemy_small.png");
        this.loadTexture("entity/basicBullet.png");
        this.loadTexture("entity/basicBulletPink.png");
        this.loadTexture("radar.png");
        this.loadTexture("tile/water5.png");
        this.loadTexture("entity/box_small.png");

        this.loadTexture("entity/large_destroyed.png");

        this.loadGroup("explosion/small/", "small_", 23);
        this.loadGroup("explosion/medium/", "medium_", 23);
        this.loadGroup("explosion/nine/", "nine_", 31);
        this.loadGroup("explosion/rocket/", "rocket_", 15);
        this.loadGroup("explosion/large/", "rocket_", 24);

        this.loadTexture("ui/bar.png");
        this.loadTexture("ui/text.png");
        this.loadTexture("ui/boss.png");
        this.loadTexture("ui/boss_hover.png");
        this.loadTexture("ui/boss_down.png");
        this.loadTexture("entity/small_destroyed.png");
        this.loadTexture("entity/playerBullet.png");

        this.loadTexture("ui/ship_double_small.png");
        this.loadTexture("ui/ship_double_small_hover.png");
        this.loadTexture("ui/ship_double_small_down.png");

        this.loadTextures("ui/boss.png", "ui/boss_down.png", "ui/boss_hover.png");
        this.loadTextures("ui/boss_locked.png", "ui/boss_down_locked.png", "ui/boss_hover_locked.png");

        this.loadTextures("tile/air.png", "tile/grass.png", "tile/stone.png", "tile/water.png");

        this.loadTexture("editorBackground.png");
        this.loadTexture("entity/test.png");

        this.loadTextures("entity/water_ripple_small_000.png", "entity/water_ripple_small_001.png", "entity/water_ripple_small_002.png", "entity/water_ripple_small_003.png", "entity/water_ripple_small_004.png");

        this.loadTexture("crosshair/crosshair6.png");

        this.loadTexture("ui/menu.png");

        this.assetManager.load("skin/holo/Holo-dark-hdpi.json", Skin.class);

        this.assetManager.load("skin/arcade/arcade-ui.json", Skin.class);


        this.assetManager.finishLoading();
    }

    public void loadGroup(String path, String prefix, int count) {
        for(int loaded = 0; loaded < count; loaded++) {
            String fileName = (path + prefix + loaded + ".png");

            this.loadTexture(fileName);
        }
    }

    /**public void loadDirectory(String path) {
        System.out.println("LOADING DIRECTORY " + path + " FOUND " + Gdx.files.internal(path).list().length + " FILES");
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
    }**/

    public void loadTexture(String path) {
        this.assetManager.load(path, Texture.class);
        System.out.println("Loaded " + path);
    }

    public void loadTextures(String ... paths) {
        for(String path : paths) {
            this.assetManager.load(path, Texture.class);
            System.out.println("Loaded " + path);
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

    public boolean update() {
        return this.assetManager.update();
    }

    public static Assets getInstance() {
        return INSTANCE;
    }

}
