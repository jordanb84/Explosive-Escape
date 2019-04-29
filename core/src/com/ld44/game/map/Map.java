package com.ld44.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.entity.impl.*;
import com.ld44.game.ship.impl.SingleCannonFrigateShip;
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

    private EntityPlayer player;

    private boolean spawnPlayer;

    private float spawnPlayerElapsed;

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
            if(entity instanceof EntityDestroyed) {
                entity.render(batch, camera);
            }
        }

        for(Entity entity : this.getEntities()) {
            if(!(entity instanceof EntityDestroyed) && !(entity instanceof EntityCrate)) {
                entity.render(batch, camera);
            }
        }

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityCrate) {
                entity.render(batch, camera);
            }
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

        if(this.spawnPlayer) {
            this.spawnPlayerElapsed += 1 * Gdx.graphics.getDeltaTime();

            if(this.spawnPlayerElapsed >= 2.5f) {
                this.spawnEntity(this.getPlayer());
                this.spawnPlayerElapsed = 0;
                this.spawnPlayer = false;
            }
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

    public void startBossBattle() {
        int centerX = mapDefinition.getMapWidth() * mapDefinition.getTileWidth() / 2;
        int centerY = mapDefinition.getMapHeight() * mapDefinition.getTileHeight() / 2;

        this.getPlayer().setHealth(1);
        this.getPlayer().getPosition().set(centerX, centerY);

        for(Entity entity : this.getEntities()) {
            if(!(entity instanceof EntityPlayer)) {
                this.despawnEntity(entity);
            }
        }

        this.getPlayer().setSpeed(0);

        EntityBoss boss = new EntityBoss(this, new Vector2(centerX + 100, centerY + 150));

        this.spawnEntity(boss);
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

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    public void spawnEnemies(boolean medium) {
        int chunkWidth = 512;
        int chunkHeight = 512;

        int mapPixelWidth = this.getMapDefinition().getMapWidth() * this.getMapDefinition().getTileWidth();
        int mapPixelHeight = this.getMapDefinition().getMapHeight() * this.getMapDefinition().getTileHeight();

        int rows = mapPixelHeight / chunkHeight;
        int columns = mapPixelWidth / chunkWidth;

        int enemySpacingWidth = 64;
        int enemySpacingHeight = 64;

        int perChunk = 2;

        if(medium) {
            perChunk = 1;
        }

        for(int chunkRow = 0; chunkRow < rows; chunkRow++) {
            for(int chunk = 0; chunk < columns; chunk++) {
                Vector2 chunkPosition = new Vector2(chunk * chunkWidth, chunkRow * chunkHeight);

                for(int enemiesSpawned = 0; enemiesSpawned < 1; enemiesSpawned++) {
                    Vector2 position = new Vector2(chunkPosition.x + enemySpacingWidth * enemiesSpawned, chunkPosition.y + (enemySpacingHeight / 2) * enemiesSpawned);

                    EntityEnemy enemy = null;

                    if(medium) {
                        enemy = new EntityMediumEnemy(this, position);
                    } else {
                        enemy = new EntityBasicEnemy(this, position);
                    }

                    this.spawnEntity(enemy);
                }
            }
        }

    }

    public void reset() {
        EntityPlayer replacementPlayer = new EntityPlayer(this, new Vector2());

        this.setPlayer(replacementPlayer);

        this.getPlayer().setPlayerShip(new SingleCannonFrigateShip(this, this.getPlayer()));
        this.getPlayer().setHealth(1);
        this.getHud().setCash(0);

        int centerX = mapDefinition.getMapWidth() * mapDefinition.getTileWidth() / 2;
        int centerY = mapDefinition.getMapHeight() * mapDefinition.getTileHeight() / 2;

        this.getPlayer().getPosition().set(centerX, centerY);
        this.getPlayer().setSpeed(0);

        this.spawnPlayer = true;
        this.spawnPlayerElapsed = 0;
    }

}
