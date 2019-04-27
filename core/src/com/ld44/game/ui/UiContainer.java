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

    public UiContainer(Skin defaultSkin, StateManager stateManager) {
        this.defaultSkin = defaultSkin;

        this.rootTable = new Table();
        this.rootTable.setFillParent(true);

        this.stateManager = stateManager;

        this.rootStage = new Stage();

        this.create();

        this.rootStage.addActor(this.rootTable);

        Gdx.input.setInputProcessor(this.rootStage);
    }

    public void render(SpriteBatch batch) {
        batch.end();
        this.rootStage.draw();
        batch.begin();
    }

    public void update(OrthographicCamera camera) {
        this.rootStage.act(Gdx.graphics.getDeltaTime());
    }

    public abstract void create();

    public void resize (int width, int height) {
        this.rootStage.getViewport().update(width, height, true);
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

}