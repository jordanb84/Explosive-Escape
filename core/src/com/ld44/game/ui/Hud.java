package com.ld44.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;

public class Hud {

    private Map map;

    private EntityPlayer player;

    private Sprite barSprite;

    private OrthographicCamera hudCamera;

    private BitmapFont font;

    private int cash = 173;

    public Hud(Map map, EntityPlayer player) {
        this.map = map;
        this.player = player;
        this.barSprite = Assets.getInstance().getSprite("ui/bar.png");

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font = new BitmapFont(Gdx.files.internal("font/shadow4.fnt"));
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(this.hudCamera.combined);

        this.barSprite.setPosition(camera.viewportWidth / 2 - this.barSprite.getWidth() / 2, camera.viewportHeight - this.barSprite.getHeight());
        this.barSprite.draw(batch);

        String cash = ("");

        int neededChars = 6;

        char[] cashChars = ("" + this.cash).toCharArray();

        for(int added = 0; added < neededChars - cashChars.length; added++) {
            cash += "0";
        }

        cash += this.cash + "";

        this.font.draw(batch, cash, this.barSprite.getX() + this.barSprite.getWidth() / 10, this.barSprite.getY() + this.barSprite.getHeight() - this.barSprite.getHeight() / 2 + this.barSprite.getHeight() / 5);

        batch.setProjectionMatrix(camera.combined);
    }

    public void update(OrthographicCamera camera) {

    }

}
