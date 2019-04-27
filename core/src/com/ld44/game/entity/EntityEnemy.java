package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.map.Map;

import java.util.Random;

public abstract class EntityEnemy extends EntityBoat {

    private float elapsedSinceDirectionChange;

    private float changeInterval;

    private Direction targetDirection;

    public EntityEnemy(Map map, Vector2 position, float speed, float maxSpeed, float speedAcceleration, float rotationSpeed) {
        super(map, position, speed, maxSpeed, speedAcceleration, rotationSpeed);
        this.changeInterval = 6;
        this.targetDirection = Direction.UP;
        this.elapsedSinceDirectionChange = this.changeInterval;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.wander();

        if(this.getSpeed() < this.getMaxSpeed() / 2) {
            this.changeSpeed(this.getMaxSpeed() / 2);
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
