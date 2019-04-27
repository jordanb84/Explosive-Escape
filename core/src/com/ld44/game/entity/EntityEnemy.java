package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.entity.impl.EntityBullet;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;

import java.util.Random;

public abstract class EntityEnemy extends EntityBoat {

    private float elapsedSinceDirectionChange;

    private float changeInterval;

    private Direction targetDirection;

    private Rectangle fireRange;

    private float fireInterval = 0.3f;

    private float elapsedSinceFire;

    public EntityEnemy(Map map, Vector2 position, float speed, float maxSpeed, float speedAcceleration, float rotationSpeed) {
        super(map, position, speed, maxSpeed, speedAcceleration, rotationSpeed);
        this.changeInterval = 6;
        this.targetDirection = Direction.UP;
        this.elapsedSinceDirectionChange = this.changeInterval;

        this.fireRange = new Rectangle();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getActiveSprite().setAlpha(this.getHealth());
        this.getActiveSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getActiveSprite().setRotation(this.getRotation());
        this.getActiveSprite().draw(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.wander();

        this.fireRange.set(this.getPosition().x - this.getWidth(), this.getPosition().y - this.getHeight(), this.getWidth() * 2, this.getHeight() * 2);

        if(this.getSpeed() < this.getMaxSpeed() / 2) {
            this.changeSpeed(this.getMaxSpeed() / 2);
        }

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();

        for(Entity entity : this.getMap().getEntities()) {
            if(entity instanceof EntityPlayer) {
                if(entity.getBody().overlaps(this.fireRange) && this.elapsedSinceFire >= this.fireInterval) {
                    EntityBullet bullet = new EntityBullet(this.getMap(), new Vector2(this.getPosition()), new Vector2(entity.getPosition()), true);

                    this.getMap().spawnEntity(bullet);
                    this.elapsedSinceFire = 0;
                }
            }
        }
    }

    private void wander() {
        Random wanderRandom = new Random();

        this.elapsedSinceDirectionChange += 1 * Gdx.graphics.getDeltaTime();

        if(this.elapsedSinceDirectionChange >= this.changeInterval) {
            this.targetDirection = Direction.getRandomDirection();
            this.elapsedSinceDirectionChange = 0;

            this.changeInterval = wanderRandom.nextFloat() * 8;

            if(targetDirection == Direction.RIGHT || targetDirection == Direction.LEFT) {
                this.changeInterval = this.changeInterval / 2;
            }

            //System.out.println("Switched to " + this.targetDirection.name());
        }

        this.changeDirection(this.targetDirection);
    }

}
