package com.ld44.game.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.impl.EntityBasicEnemy;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.map.MapDefinition;
import com.ld44.game.map.MapLayer;
import com.ld44.game.state.State;
import com.ld44.game.state.StateManager;
import com.ld44.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class StateTest extends State {

    private Map map;

    private BitmapFont font;

    public StateTest(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {

        MapDefinition mapDefinition = new MapDefinition(160, 120, 16, 16, 1);

        List<MapLayer> tileLayers = new ArrayList<MapLayer>();
        tileLayers.add(new MapLayer(TileType.Water, mapDefinition));
        tileLayers.add(new MapLayer(TileType.Air, mapDefinition));

        List<Entity> entities = new ArrayList<Entity>();

        this.map = new Map(tileLayers, mapDefinition, entities);

        int centerX = mapDefinition.getMapWidth() * mapDefinition.getTileWidth() / 2;
        int centerY = mapDefinition.getMapHeight() * mapDefinition.getTileHeight() / 2;

        this.map.spawnEntity(new EntityPlayer(this.map, new Vector2(centerX, centerY)));
        this.map.spawnEntity(new EntityBasicEnemy(this.map, new Vector2(centerX + 50, centerY - 50)));

        this.font = new BitmapFont(Gdx.files.internal("font/large.fnt"));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.map.render(batch, camera);
        this.font.draw(batch, "heyy", 100, 100);
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.map.update(camera);

    }

    @Override
    public void resize(int width, int height) {

    }

}
