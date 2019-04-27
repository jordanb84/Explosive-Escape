package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private boolean chasing;

    private float targetRotation;

    private ShapeRenderer shapeRenderer;

    private Vector2 spawnPosition;

    public EntityEnemy(Map map, Vector2 position, float speed, float maxSpeed, float speedAcceleration, float rotationSpeed) {
        super(map, position, speed, maxSpeed, speedAcceleration, rotationSpeed);
        this.changeInterval = 6;
        this.targetDirection = Direction.UP;
        this.elapsedSinceDirectionChange = this.changeInterval;

        this.fireRange = new Rectangle();

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);

        this.spawnPosition = new Vector2(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getActiveSprite().setAlpha(this.getHealth());
        this.getActiveSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getActiveSprite().setRotation(this.getRotation());
        this.getActiveSprite().draw(batch);

        batch.end();
        this.shapeRenderer.begin();
        this.shapeRenderer.setProjectionMatrix(camera.combined);
        this.shapeRenderer.rect(this.fireRange.x, this.fireRange.y, this.fireRange.width, this.fireRange.height);
        this.shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.wander();

        this.chasing = false;

        //this.fireRange.set(this.getPosition().x - (this.getWidth() * 2 + this.getWidth() / 2), this.getPosition().y - (this.getHeight() * 2 + this.getHeight() / 2), this.getWidth() * 6, this.getHeight() * 6);
        //if the player can see us
        this.fireRange.set(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);

        if(this.getSpeed() < this.getMaxSpeed() / 2) {
            this.changeSpeed(this.getMaxSpeed() / 2);
        }

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();

        for(Entity entity : this.getMap().getEntities()) {
            if(entity instanceof EntityPlayer) {
                if(this.getBody().overlaps(this.fireRange)) {
                    if(this.elapsedSinceFire >= this.fireInterval){
                        EntityBullet bullet = new EntityBullet(this.getMap(), new Vector2(this.getPosition()), new Vector2(entity.getPosition()), true);

                        this.getMap().spawnEntity(bullet);
                        this.elapsedSinceFire = 0;
                    }

                    this.chasing = true;
                    this.targetRotation = this.rotationToPoint(new Vector2(entity.getPosition()));
                }
            }
        }

        if(this.chasing) {
            float diff = this.targetRotation - this.getRotation();

            //System.out.println("Diff " + diff + " target " + this.targetRotation + " current " + this.getRotation());

                float rotationModifier = (this.getRotationSpeed() * 3) * Gdx.graphics.getDeltaTime();
                if (this.targetRotation > this.getRotation()) {
                    this.setRotation(this.getRotation() + rotationModifier);
                }

                if (this.targetRotation < this.getRotation()) {
                    this.setRotation(this.getRotation() - rotationModifier);
                }
            }
    }

    private void wander() {
        if(!this.chasing) {
            Random wanderRandom = new Random();

            this.elapsedSinceDirectionChange += 1 * Gdx.graphics.getDeltaTime();

            if (this.elapsedSinceDirectionChange >= this.changeInterval) {
                this.targetDirection = Direction.getRandomDirection();
                this.elapsedSinceDirectionChange = 0;

                this.changeInterval = wanderRandom.nextFloat() * 8;

                if (targetDirection == Direction.RIGHT || targetDirection == Direction.LEFT) {
                    this.changeInterval = this.changeInterval / 2;
                }

                //System.out.println("Switched to " + this.targetDirection.name());
            }

            this.changeDirection(this.targetDirection);
        }
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    @Override
    public void reset() {
        super.reset();
        this.getPosition().set(this.getSpawnPosition().x, this.getSpawnPosition().y);
    }

}