package com.ld44.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class StateManager {

    private HashMap<String, State> states = new HashMap<String, State>();

    private State activeState;

    public StateManager() {

    }

    public void renderActiveState(SpriteBatch batch, OrthographicCamera camera) {
        this.getActiveState().render(batch, camera);
    }

    public void updateActiveState(OrthographicCamera camera) {
        this.getActiveState().update(camera);
    }

    public void resizeActiveState(int width, int height) {
        this.activeState.resize(width, height);
    }

    public void registerState(String name, State state) {
        this.states.put(name, state);
    }

    public State getActiveState() {
        return this.activeState;
    }

    public void setActiveState(String name) {
        this.activeState = this.states.get(name);
    }

    public void setActiveState(State state) {
        this.activeState = state;
    }

}
