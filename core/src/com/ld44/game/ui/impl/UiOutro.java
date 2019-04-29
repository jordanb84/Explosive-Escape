package com.ld44.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ld44.game.audio.MusicType;
import com.ld44.game.map.Map;
import com.ld44.game.ui.Hud;

public class UiOutro extends UiIntro {

    private Map map;

    public UiOutro(Hud hud, Map map) {
        super(hud);
        this.map = map;
    }

    @Override
    public void create() {
        this.getRootTable().setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);

        this.setIntro(new Label("", this.getDefaultSkin()));
        this.getIntro().setWrap(true);
        this.getIntro().setAlignment(Align.center);

        this.getRootTable().add(this.getIntro()).width(Gdx.graphics.getWidth()).height(300);

        this.setIntroText("Well damn. Even after my boss takes his cut, I'll have quite a fortune here and can afford to live for more than just killing and stealing currency for somebody else. [Press ESCAPE to Leave]");
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        System.out.println(this.isFinished());
        if(this.isFinished()) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                this.getHud().getStateManager().setActiveState("menu");
                this.getHud().getPlayer().getMap().reset(false);
                this.map.setWon(false);
                this.map.getHud().getStore().resetLocks();
                MusicType.stopMusic();
                this.restart();
            }
        }
    }

    @Override
    public Hud getHud() {
        return this.map.getHud();
    }

}
