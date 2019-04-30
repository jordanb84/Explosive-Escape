package com.ld44.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ld44.game.assets.Assets;
import com.ld44.game.audio.MusicType;
import com.ld44.game.audio.SoundEffectType;
import com.ld44.game.state.StateManager;
import com.ld44.game.state.impl.StateTest;
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.Skins;
import com.ld44.game.ui.UiContainer;

public class UiMenu extends UiContainer {

    private Sprite background;

    public UiMenu(Hud hud, StateManager stateManager) {
        super(hud, Skins.Holo_Dark_Hdpi.SKIN, stateManager, true);
    }

    @Override
    public void create() {
        this.background = Assets.getInstance().getSprite("ui/menu.png");
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.getRootTable().addActor(new ImageButton(new SpriteDrawable(this.background)));

        TextButton startButton = new TextButton("Play", this.getDefaultSkin());

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(!getStateManager().isPlaying()) {
                    //getStateManager().setActiveState(new StateTest(getStateManager()));
                    getStateManager().setActiveState("test");
                    SoundEffectType.playSound(SoundEffectType.Crash);
                    MusicType.loopMusic(MusicType.Background);
                    getStateManager().setPlaying(true);
                }
            }
        });

        TextButton controlsButton = new TextButton("Controls", this.getDefaultSkin());

        String controls = ("Accelerate/Decelerate: W/S or UP/DOWN" + "\nTurn: A/D or LEFT/RIGHT" + "\nFire: Hold either F, SPACE, J or Left Click");

        TextTooltip controlsTooltip = new TextTooltip(controls, Skins.Arcade.SKIN);
        controlsTooltip.setInstant(true);

        controlsButton.addListener(controlsTooltip);

        TextButton creditsButton = new TextButton("Credits", this.getDefaultSkin());

        String credits = ("Written in 72 hours by exilegl (jordanb84)");
        credits += ("\n\n\nAssets from OpenGameArt.org by these users:\n");
        credits += ("\nChabull: Ships, HUD, Explosions (CC-BY 3.0)");
        credits += ("\nQubodup: Water (CC0)");
        credits += ("\nHc: Crosshairs (CC0)");
        credits += ("\nMaster484: Bullets (CC0)");
        credits += ("\n\nSFX/Music:");
        credits += ("\nJoth: Explosion (CC0)");
        credits += ("\nMvrasseli: Main Music (CC-BY 3.0)");
        credits += ("\nYd: Boss Music (CC0)");
        credits += ("\nQubodup: Click sound 1 & 2 (CC0)");
        //credits += ("\nThemightyglider: Click sound 2 (CC0)");

        TextTooltip creditsTooltip = new TextTooltip(credits, Skins.Arcade.SKIN);
        creditsTooltip.setInstant(true);

        creditsButton.addListener(creditsTooltip);

        TextButton exitButton = new TextButton("Exit", this.getDefaultSkin());

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(!getStateManager().isPlaying()) {
                    System.exit(0);
                }
            }
        });

        //startButton.setSize(50, 50);


        this.getRootTable().add(startButton).center().fillX();
        this.getRootTable().row();
        this.getRootTable().add(controlsButton).center().fillX();
        this.getRootTable().row();
        this.getRootTable().add(creditsButton).center().fillX();
        this.getRootTable().row();
        this.getRootTable().add(exitButton).center().fillX();
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        Gdx.input.setInputProcessor(this.getStage());
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

}
