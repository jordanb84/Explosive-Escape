package com.ld44.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {

    private StateManager manager;

    public State(StateManager manager) {
        this.manager = manager;
        this.create();
    }

    public abstract void create();

    public abstract void render(SpriteBatch batch, OrthographicCamera camera);

    public abstract void update(OrthographicCamera camera);

    public abstract void resize(int width, int height);

    public StateManager getManager() {
        return this.manager;
    }

}
