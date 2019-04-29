package com.ld44.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class StateManager {

    private HashMap<String, State> states = new HashMap<String, State>();

    private State activeState;

    private OrthographicCamera hudCamera;

    public StateManager() {
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void renderActiveState(SpriteBatch batch, OrthographicCamera camera) {
        this.getActiveState().render(batch, camera);
    }

    public void updateActiveState(OrthographicCamera camera) {
        this.getActiveState().update(camera);
    }

    private int resizeWidth = 0;
    private int resizeHeight = 0;

    public void resizeActiveState(int width, int height) {
        this.activeState.resize(width, height);
        this.resizeWidth = width;
        this.resizeHeight = height;

        //System.out.println("Resize set to " + this.resizeWidth + "/" + this.resizeHeight);

        this.resizeAllStates(width, height);
    }

    public void resizeAllStates(int width, int height) {
        for(State state : this.states.values()) {
            state.resize(width, height);
            //System.out.println("Resized " + state.toString());
        }
    }

    public void registerState(String name, State state) {
        this.states.put(name, state);
    }

    public State getActiveState() {
        return this.activeState;
    }

    public void setActiveState(String name) {
        this.activeState = this.states.get(name);
        /**if(this.resizeWidth > 0 && this.resizeHeight > 0) {
            this.activeState.resize(this.resizeWidth, this.resizeHeight);
        }**/
    }

    public void setActiveState(State state) {
        this.activeState = state;

        /**if(this.resizeWidth > 0 && this.resizeHeight > 0) {
            state.resize(this.resizeWidth, this.resizeHeight);
        }**/
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

}
