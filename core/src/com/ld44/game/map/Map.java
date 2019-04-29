package com.ld44.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.ld44.game.ui.impl.UiOutro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private Sprite destroyedPlayer;

    private boolean won;

    private BitmapFont font;

    private UiOutro outro;

    public Map(List<MapLayer> tileLayers, MapDefinition mapDefinition, List<Entity> entities) {
        this.tileLayers = tileLayers;
        this.mapDefinition = mapDefinition;
        this.entities = entities;
        this.crosshair = Assets.getInstance().getSprite("crosshair/crosshair6.png");
        this.crosshair.setAlpha(0.8f);
        this.hud = hud;

        this.font = new BitmapFont(Gdx.files.internal("font/shadow4.fnt"));
        this.outro = new UiOutro(this.hud, this);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(MapLayer tileLayer : this.getTileLayers()) {
            tileLayer.render(batch, camera);
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

        if(this.spawnPlayer && this.destroyedPlayer != null) {
            this.destroyedPlayer.draw(batch);
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

        if(this.won) {
            //this.font.draw(batch, "You win!", camera.position.x - 70, camera.position.y);

            this.outro.render(batch);
        }
    }

    public void update(OrthographicCamera camera) {
        if(this.won) {
            this.outro.update(camera);
        }

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

    public void win(Vector2 position, OrthographicCamera camera) {
        this.won = true;

        this.hud.modifyCash(40000 + new Random().nextInt(11584));

        int rows = 8;
        int columns = 15;

        Vector2 spread = new Vector2(700, 700);

        String spritePath = ("explosion/nine/nine_");
        String spritePathMedium = ("explosion/medium/medium_");

        List<EntityExplosion> explosions = this.generateExplosionBall(rows, columns, new Vector2(camera.position.x - camera.viewportWidth + camera.viewportWidth / 4, camera.position.y - camera.viewportHeight + camera.viewportHeight / 8), spread, spritePath);
        List<EntityExplosion> explosionsMedium = this.generateExplosionBall(rows, columns, new Vector2(camera.position.x - camera.viewportWidth + camera.viewportWidth / 4, camera.position.y - camera.viewportHeight + camera.viewportHeight / 8), spread, spritePathMedium);

        explosions.addAll(explosionsMedium);

        Random random = new Random();

        for(EntityExplosion explosion : explosions) {
            explosion.setDelay(random.nextFloat() * 6);
            this.spawnEntity(explosion);
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

    public void reset(boolean destroyPlayer) {
        this.getHud().getStore().resetLocks();

        this.despawnEntity(this.getPlayer());

        if(destroyPlayer) {
            this.destroyedPlayer = this.getPlayer().getPlayerShip().getDestroyedSprite();
            this.destroyedPlayer.setRotation(this.getPlayer().getRotation());
            this.destroyedPlayer.setPosition(this.getPlayer().getPosition().x, this.getPlayer().getPosition().y);
        }

        EntityPlayer replacementPlayer = new EntityPlayer(this, new Vector2());

        this.setPlayer(replacementPlayer);

        this.getPlayer().setPlayerShip(new SingleCannonFrigateShip(this, this.getPlayer()));
        this.getPlayer().setHealth(1);
        this.getHud().setCash(0);

        this.centerPlayer();

        this.spawnPlayer = true;
        this.spawnPlayerElapsed = 0;

        if(!destroyPlayer) {
            this.spawnPlayerElapsed = 2.5f;
        }

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityEnemy || entity instanceof EntityBullet || entity instanceof EntityDestroyed || entity instanceof EntityCrate) {
                this.despawnEntity(entity);
            }
        }

        this.spawnEnemies(false);
    }

    public void centerPlayer() {
        int centerX = mapDefinition.getMapWidth() * mapDefinition.getTileWidth() / 2;
        int centerY = mapDefinition.getMapHeight() * mapDefinition.getTileHeight() / 2;

        this.getPlayer().getPosition().set(centerX, centerY);
        this.getPlayer().setSpeed(0);
    }

    public List<EntityExplosion> generateExplosionBall(int rows, int columns, Vector2 position, Vector2 spread, String spritePath) {
        List<EntityExplosion> explosions = new ArrayList<EntityExplosion>();

        Random random = new Random();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int offX = random.nextInt((int) spread.x);
                int offY = random.nextInt((int) spread.y);

                if (offX < spread.x / 2) {
                    offX = (int) spread.x / 2;
                }

                if (offY < spread.y / 2) {
                    offY = (int) spread.y / 2;
                }

                Vector2 offset = new Vector2(offX, offY);

                Vector2 offsetPosition = new Vector2(position.x + offset.x, position.y + offset.y);

                EntityExplosion explosion = new EntityExplosion(this, offsetPosition, null, spritePath);

                explosions.add(explosion);
            }
        }

        return explosions;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

}
