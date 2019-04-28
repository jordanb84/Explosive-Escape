package com.ld44.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.ui.impl.UiIntro;
import com.ld44.game.ui.impl.UiStore;
import com.ld44.game.ui.impl.Unlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hud {

    private Map map;

    private EntityPlayer player;

    private Sprite barSprite;

    private OrthographicCamera hudCamera;

    private BitmapFont font;

    private BitmapFont flashFont;

    private BitmapFont mediumFont;

    private int cash = 0;

    private float flashElapsed;
    private float flashDuration = 0.8f;

    private boolean flash;

    private Sprite statsBar;

    private Sprite textSprite;

    private UiStore uiStore;

    private UiIntro uiIntro;

    public Hud(Map map, EntityPlayer player) {
        this.map = map;
        this.player = player;
        this.barSprite = Assets.getInstance().getSprite("ui/text.png");
        this.statsBar = Assets.getInstance().getSprite("ui/bar.png");
        this.textSprite = Assets.getInstance().getSprite("ui/text.png");

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font = new BitmapFont(Gdx.files.internal("font/shadow4.fnt"));
        this.flashFont = new BitmapFont(Gdx.files.internal("font/flashGreen.fnt"));
        this.mediumFont = new BitmapFont(Gdx.files.internal("font/greyMedium.fnt"));

        this.uiStore = new UiStore(this);

        this.uiIntro = new UiIntro(this);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(this.hudCamera.combined);

        /**for(int bar = 0; bar < this.hudCamera.viewportWidth / this.statsBar.getWidth(); bar++) {
            this.statsBar.setPosition(bar * this.statsBar.getWidth(), this.hudCamera.viewportHeight - this.statsBar.getHeight());
            this.statsBar.draw(batch);
        }**/

        if(!this.uiIntro.isActive()) {
            this.textSprite.setPosition(camera.viewportWidth / 2 - this.barSprite.getWidth() / 2, camera.viewportHeight - this.barSprite.getHeight());

            this.barSprite.setPosition(this.textSprite.getX(), this.textSprite.getY() - this.barSprite.getHeight());
            //this.barSprite.draw(batch);

            this.textSprite.draw(batch);
            this.barSprite.draw(batch);

            String cash = ("");

            int neededChars = 6;

            char[] cashChars = ("" + this.cash).toCharArray();

            for (int added = 0; added < neededChars - cashChars.length; added++) {
                cash += "0";
            }

            cash += this.cash + "";

            BitmapFont font = this.mediumFont;

            if (this.flash) {
                font = this.flashFont;

                this.flashElapsed += 1 * Gdx.graphics.getDeltaTime();

                if (this.flashElapsed >= this.flashDuration) {
                    this.flashElapsed = 0;
                    this.flash = false;
                }
            }

            font.draw(batch, "Balance", this.textSprite.getX() + this.textSprite.getWidth() / 8 + 10, this.textSprite.getY() + this.textSprite.getHeight() / 2 + this.textSprite.getHeight() / 4 - 5);
            font.draw(batch, cash, this.barSprite.getX() + this.barSprite.getWidth() / 4 - this.textSprite.getWidth() / 16 + 2, this.barSprite.getY() + this.barSprite.getHeight() - this.barSprite.getHeight() / 2 + this.barSprite.getHeight() / 5 - 2);

            this.uiStore.render(batch);

            this.textSprite.setPosition(10, 10);
            this.textSprite.draw(batch);
            this.mediumFont.draw(batch, "Store", this.textSprite.getX() + this.textSprite.getWidth() / 6 + 22, this.textSprite.getY() + this.textSprite.getHeight() / 2 + this.textSprite.getHeight() / 4 - 5);
        } else {
            this.uiIntro.render(batch);
        }

        batch.setProjectionMatrix(camera.combined);
    }

    public void start() {
        this.map.spawnEnemies();
    }

    public void update(OrthographicCamera camera) {
        this.uiIntro.update(camera);
        this.uiStore.update(camera);
    }

    public void modifyCash(int amount) {
        this.cash += amount;

        this.flash = true;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public int getCash() {
        return cash;
    }

}
