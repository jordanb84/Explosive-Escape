package com.ld44.game.state.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ld44.game.state.State;
import com.ld44.game.state.StateManager;
import com.ld44.game.ui.impl.UiMenu;

public class StateMenu extends State {

    private UiMenu uiMenu;

    public StateMenu(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {
        this.uiMenu = new UiMenu(null, this.getManager());
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.uiMenu.render(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.uiMenu.update(camera);
    }

    @Override
    public void resize(int width, int height) {

    }

}
