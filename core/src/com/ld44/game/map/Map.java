package com.ld44.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.ui.Hud;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<MapLayer> tileLayers = new ArrayList<MapLayer>();

    private MapDefinition mapDefinition;

    private List<Entity> entities = new ArrayList<Entity>();

    private List<Entity> entitySpawnQueue = new ArrayList<Entity>();
    private List<Entity> entityDespawnQueue = new ArrayList<Entity>();

    private Sprite crosshair;

    private Hud hud;

    public Map(List<MapLayer> tileLayers, MapDefinition mapDefinition, List<Entity> entities) {
        this.tileLayers = tileLayers;
        this.mapDefinition = mapDefinition;
        this.entities = entities;
        this.crosshair = Assets.getInstance().getSprite("crosshair/crosshair6.png");
        this.crosshair.setAlpha(0.8f);
        this.hud = hud;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(MapLayer tileLayer : this.getTileLayers()) {
            tileLayer.render(batch);
        }

        for(Entity entity : this.getEntities()) {
            entity.render(batch, camera);
        }

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        this.crosshair.setPosition(mousePosition.x - this.crosshair.getWidth() / 2, mousePosition.y - this.crosshair.getHeight() / 2);
        this.crosshair.draw(batch);
    }

    public void update(OrthographicCamera camera) {
        for(MapLayer tileLayer : this.getTileLayers()) {
            tileLayer.update();
        }

        this.entities.addAll(this.entitySpawnQueue);

        for(Entity entity : this.entityDespawnQueue) {
            this.entities.remove(entity);
        }

        this.entitySpawnQueue.clear();
        this.entityDespawnQueue.clear();

        for(Entity entity : this.entities) {
            entity.update(camera);
        }
    }

    public void spawnEntity(Entity entity) {
        this.entitySpawnQueue.add(entity);
    }

    public void despawnEntity(Entity entity) {
        this.entityDespawnQueue.add(entity);
    }

    public List<MapLayer> getTileLayers() {
        return tileLayers;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }

    public Hud getHud() {
        return hud;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

}
