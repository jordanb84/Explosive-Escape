package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.map.Map;

import java.util.Random;

public class EntityBullet extends Entity {

    private Vector2 destination;

    private Rectangle destinationBody;

    private boolean enemy;

    private int threshold = 1;

    private String explosionSpritePath;

    private Sprite sprite;

    //act as homing bullets for the first second or so, so the player cant stand under the boss at slow speed and not get hit
    private float lifeElapsed;

    private float range;

    private boolean shouldMove;

    private float lifespan;

    private float damageMultiplier = 1;

    public EntityBullet(Map map, Vector2 position, Vector2 destination, boolean enemy, String explosionSpritePath) {
        super(map, position, 160);
        this.destination = destination;
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, this.threshold, this.threshold);
        this.enemy = enemy;

        if(enemy) {
            this.setSpeed(this.getSpeed() * 2);
            this.sprite = Assets.getInstance().getSprite("entity/basicBullet.png");
        } else {
            this.sprite = Assets.getInstance().getSprite("entity/basicBullet.png");
        }

        this.explosionSpritePath = explosionSpritePath;

        this.shouldMove = true;
        this.lifespan = 8;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        //super.render(batch, camera);
        this.sprite.setPosition(this.getPosition().x, this.getPosition().y);
        this.sprite.setAlpha(0.9f);
        this.sprite.draw(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        if(this.lifeElapsed >= this.lifespan) {
            this.getMap().despawnEntity(this);
        }

        this.lifeElapsed += 1 * Gdx.graphics.getDeltaTime();

        if(this.enemy && this.lifeElapsed < this.getRange()) {
            this.destination = new Vector2(this.getMap().getPlayer().getPosition());
        }

        if(this.shouldMove) {
            this.moveTowardDestination(this.destination, this.threshold);
        }

        boolean hit = false;

        Entity hitEntity = null;

        if(enemy) {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityPlayer) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        ((EntityPlayer) entity).damage(0.002f * this.getDamageMultiplier());
                        hit = true;
                        hitEntity = entity;
                        //System.out.println("Hit player");
                    }
                }
            }
        } else {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityEnemy) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        //entity.setHealth(entity.getHealth() - 0.1f);
                        ((EntityEnemy) entity).damage(0.1f);
                        hit = true;
                        hitEntity = entity;
                    }
                }
            }
        }

        if(hit) {
            EntityExplosion entityExplosion = new EntityExplosion(this.getMap(), this.getPosition(), hitEntity, this.explosionSpritePath);
            this.getMap().spawnEntity(entityExplosion);

            this.getMap().despawnEntity(this);
        }

        this.destinationBody.set(this.destination.x, this.destination.y, this.destinationBody.width, this.destinationBody.height);

        if(this.getBody().overlaps(this.destinationBody)) {
            this.getMap().despawnEntity(this);
        }
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(1, "entity/basicBullet.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        return directionalAnimation;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public void setShouldMove(boolean shouldMove) {
        this.shouldMove = shouldMove;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setLifespan(float lifespan) {
        this.lifespan = lifespan;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

}
