package com.ld44.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld44.game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<MapLayer> tileLayers = new ArrayList<MapLayer>();

    private MapDefinition mapDefinition;

    private List<Entity> entities = new ArrayList<Entity>();

    private List<Entity> entitySpawnQueue = new ArrayList<Entity>();
    private List<Entity> entityDespawnQueue = new ArrayList<Entity>();

    public Map(List<MapLayer> tileLayers, MapDefinition mapDefinition, List<Entity> entities) {
        this.tileLayers = tileLayers;
        this.mapDefinition = mapDefinition;
        this.entities = entities;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(MapLayer tileLayer : this.getTileLayers()) {
            tileLayer.render(batch);
        }

        for(Entity entity : this.getEntities()) {
            entity.render(batch, camera);
        }
    }

    public void update(OrthographicCamera camera) {
        for(MapLayer tileLayer : this.getTileLayers()) {
            tileLayer.update();
        }

        this.entities.addAll(this.entitySpawnQueue);
        this.entities.removeAll(this.entityDespawnQueue);

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

}
