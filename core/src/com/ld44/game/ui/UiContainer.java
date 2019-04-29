package com.ld44.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ld44.game.state.StateManager;

public abstract class UiContainer {

    private Skin defaultSkin;

    private Table rootTable;

    private Stage rootStage;

    private StateManager stateManager;

    private Hud hud;

    public UiContainer(Hud hud, Skin defaultSkin, StateManager stateManager, boolean takeInput) {
        this.defaultSkin = defaultSkin;

        this.rootTable = new Table();
        this.rootTable.setFillParent(true);

        this.stateManager = stateManager;

        this.rootStage = new Stage();

        this.hud = hud;

        this.create();

        this.rootStage.addActor(this.rootTable);

        if(takeInput) {
            Gdx.input.setInputProcessor(this.rootStage);
        }
    }

    private int width = 0;
    private int height = 0;

    public void render(SpriteBatch batch) {
        batch.end();
        this.rootStage.draw();
        batch.begin();
    }

    public void update(OrthographicCamera camera) {
        this.rootStage.act(Gdx.graphics.getDeltaTime());

        if(width > 0 && height > 0) {
            //System.out.println("UPDATE WH " + width + "/" + height);
            this.rootStage.getViewport().update(width, height, true);
        }
    }

    /**
     * If you update when the store isn't active yet, positions dont get updated right,
     * but the buttons/hovers etc are fine (maybe just because positions didnt move)
     *
     * since the hud for the store makes a _new_ camera, which is made _After_ you hit play,
     * maybe that camera isn't up to date
     */

    public abstract void create();

    public void resize (int width, int height) {
        this.rootStage.getViewport().update(width, height, true);
        this.width = width;
        this.height = height;
        //this.rootStage.getViewport().setScreenSize(width, height);
    }

    public Skin getDefaultSkin() {
        return this.defaultSkin;
    }

    public Table getRootTable() {
        return rootTable;
    }

    public Stage getStage() {
        return this.rootStage;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public Hud getHud() {
        return hud;
    }

}