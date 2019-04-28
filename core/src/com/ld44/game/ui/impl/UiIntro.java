package com.ld44.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ld44.game.assets.Assets;
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.Skins;
import com.ld44.game.ui.UiContainer;

public class UiIntro extends UiContainer {

    private String introText = ("We brought you here to loot and plunder as much currency as you're able before dying.\n" +
            "Your life exists for the sole purpose of generating profit for this organization. If you want your freedom, you'll have to " +
            "destroy the boss vessel, worth thousands in gold. Good luck! [SPACE to Start]");

    //Now that that you're here, it seems you deserve a quick explanation.
    //It's unfortunate we had to do this, but times are tough.
    private float scrollElapsed = 0;

    private Label intro;

    private int characters = 0;

    private Sprite background;

    private boolean active;

    //add bg based on that grey wall sprite

    public UiIntro(Hud hud) {
        super(hud, Skins.Holo_Dark_Hdpi.SKIN, null);
    }

    @Override
    public void create() {
        this.getRootTable().setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);

        this.background = Assets.getInstance().getSprite("editorBackground.png");
        this.background.setAlpha(0.5f);
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);

        SpriteDrawable backgroundDrawable = new SpriteDrawable(this.background);

        this.intro = new Label("", this.getDefaultSkin());
        this.intro.setWrap(true);
        this.intro.setAlignment(Align.center);
        //this.intro.setPosition(intro.getX() - 50, intro.getY() + Gdx.graphics.getHeight() / 3);

       // this.getRootTable().add(new ImageButton(backgroundDrawable));

        this.getRootTable().add(this.intro).width(Gdx.graphics.getWidth()).height(300);

        //this.getStage().setDebugAll(true);

        this.active = true;
    }

    @Override
    public void update(OrthographicCamera camera) {
        if(this.isActive()) {
            this.scrollElapsed += 1 * Gdx.graphics.getDeltaTime();

            if (this.scrollElapsed >= 0.06f && this.characters < this.introText.length()) {
                this.intro.setText(this.intro.getText().toString() + this.introText.toCharArray()[this.characters]);
                this.characters++;
                this.scrollElapsed = 0;
            }

            if (this.characters >= this.introText.length()) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    this.active = false;
                    this.getHud().start();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                this.active = false;
                this.getHud().start();
            }

            super.update(camera);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public boolean isActive() {
        return this.active;
    }

}
