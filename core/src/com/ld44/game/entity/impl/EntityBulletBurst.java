package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityBulletBurst extends Entity {

    private Vector2 destination;

    private List<EntityBullet> burst = new ArrayList<EntityBullet>();

    private Vector2 spread; //100 50

    private float burstSpeed;

    private String explosion;

    public EntityBulletBurst(float damageMultiplier, String explosion, int rows, int columns, Map map, Vector2 position, Vector2 destination, Vector2 spread, float burstSpeed, Sprite bulletSprite) {
        super(map, position, 100);
        this.spread = spread;
        this.burstSpeed = burstSpeed;
        this.destination = destination;
        this.explosion = explosion;
        this.burst = this.generateBulletBall(rows, columns, position, destination);
        this.setSpeed(this.burstSpeed);

        for(EntityBullet bullet : this.burst) {
            bullet.setShouldMove(false);
            bullet.setRange(3);
            bullet.setSprite(bulletSprite);
            bullet.setSpeed(this.burstSpeed * 2.5f);
            bullet.setDamageMultiplier(damageMultiplier * 2);
            this.getMap().spawnEntity(bullet);
        }
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

    }

    @Override
    public void update(OrthographicCamera camera) {
        Vector2 force = new Vector2();

        float delta = Gdx.graphics.getDeltaTime();

        if(this.getPosition().x < this.destination.x) {
            force.add(this.getSpeed() * delta, 0);
        }

        if(this.getPosition().x > this.destination.x) {
            force.add(-this.getSpeed() * delta, 0);
        }

        if(this.getPosition().y < this.destination.y) {
            force.add(0, this.getSpeed() * delta);
        }

        if(this.getPosition().y > this.destination.y) {
            force.add(0, -this.getSpeed() * delta);
        }

        for(EntityBullet bullet : this.burst) {
            bullet.getPosition().add(force);
        }
    }

    @Override
    public DirectionalAnimation createAnimation() {
        return null;
    }

    public List<EntityBullet> generateBulletBall(int rows, int columns, Vector2 origin, Vector2 destination) {
        List<EntityBullet> bullets = new ArrayList<EntityBullet>();

        Random random = new Random();

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                Vector2 offset = new Vector2(random.nextInt((int) this.spread.x), random.nextInt((int) this.spread.y));

                Vector2 position = new Vector2(origin.x + offset.x, origin.y + offset.y);

                EntityBullet bullet = new EntityBullet(this.getMap(), position, destination, true, this.explosion);

                bullets.add(bullet);
            }
        }

        return bullets;
    }

}
