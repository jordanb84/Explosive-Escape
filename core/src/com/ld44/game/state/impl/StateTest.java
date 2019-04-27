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
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.MiniMap;

import java.util.ArrayList;
import java.util.List;

public class StateTest extends State {

    private Map map;

    private BitmapFont font;

    private MiniMap miniMap;

    private Hud hud;

    public StateTest(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {

        MapDefinition mapDefinition = new MapDefinition(20, 15, 512, 512, 1);

        List<MapLayer> tileLayers = new ArrayList<MapLayer>();
        tileLayers.add(new MapLayer(TileType.Water, mapDefinition));
        tileLayers.add(new MapLayer(TileType.Air, mapDefinition));

        List<Entity> entities = new ArrayList<Entity>();

        this.map = new Map(tileLayers, mapDefinition, entities);

        int centerX = mapDefinition.getMapWidth() * mapDefinition.getTileWidth() / 2;
        int centerY = mapDefinition.getMapHeight() * mapDefinition.getTileHeight() / 2;

        EntityPlayer player = new EntityPlayer(this.map, new Vector2(centerX, centerY));
        this.map.spawnEntity(player);

        this.font = new BitmapFont(Gdx.files.internal("font/large.fnt"));

        this.spawnEnemies();

        this.miniMap = new MiniMap(this.map);

        this.hud = new Hud(this.map, player);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.map.render(batch, camera);
        //this.font.draw(batch, "heyy", 100, 100);
        this.hud.render(batch, camera);
       // this.miniMap.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.map.update(camera);
        this.miniMap.update(camera);
        this.hud.update(camera);
    }

    @Override
    public void resize(int width, int height) {

    }

    public void spawnEnemies() {
        int chunkWidth = 512;
        int chunkHeight = 512;

        int mapPixelWidth = map.getMapDefinition().getMapWidth() * map.getMapDefinition().getTileWidth();
        int mapPixelHeight = map.getMapDefinition().getMapHeight() * map.getMapDefinition().getTileHeight();

        int rows = mapPixelHeight / chunkHeight;
        int columns = mapPixelWidth / chunkWidth;

        int enemySpacingWidth = 64;
        int enemySpacingHeight = 64;

        for(int chunkRow = 0; chunkRow < rows; chunkRow++) {
            for(int chunk = 0; chunk < columns; chunk++) {
                Vector2 chunkPosition = new Vector2(chunk * chunkWidth, chunkRow * chunkHeight);

                for(int enemiesSpawned = 0; enemiesSpawned < 2; enemiesSpawned++) {
                    Vector2 position = new Vector2(chunkPosition.x + enemySpacingWidth * enemiesSpawned, chunkPosition.y + (enemySpacingHeight / 2) * enemiesSpawned);
                    EntityBasicEnemy entityBasicEnemy = new EntityBasicEnemy(this.map, position);
                    this.map.spawnEntity(entityBasicEnemy);
                }
            }
        }

    }

}
