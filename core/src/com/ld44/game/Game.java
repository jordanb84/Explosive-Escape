package com.ld44.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld44.game.assets.Assets;
import com.ld44.game.state.StateManager;
import com.ld44.game.state.editor.StateEditor;
import com.ld44.game.state.impl.StateTest;

public class Game extends ApplicationAdapter {

	private StateManager stateManager;

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private boolean loaded;

	@Override
	public void create () {
		Assets.getInstance().loadAssets();

		this.batch = new SpriteBatch();

		this.camera = new OrthographicCamera();
		//this.camera.setToOrtho(false, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.graphics.setTitle("LD44");
	}

	@Override
	public void render () {
		if(Assets.getInstance().update()) {
			if(!this.loaded) {
				this.load();
			}

			this.stateManager.updateActiveState(this.camera);

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			this.batch.setProjectionMatrix(this.camera.combined);
			this.batch.begin();
			this.stateManager.renderActiveState(this.batch, this.camera);
			this.batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	public void load() {
		this.stateManager = new StateManager();

		this.stateManager.registerState("test", new StateTest(this.stateManager));
		this.stateManager.registerState("editor", new StateEditor(this.stateManager));
		this.stateManager.setActiveState("test");

		this.loaded = true;
	}

}