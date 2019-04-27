package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.map.Map;

public class EntityBullet extends Entity {

    private Vector2 destination;

    private Rectangle destinationBody;

    private boolean enemy;

    private int threshold = 1;

    public EntityBullet(Map map, Vector2 position, Vector2 destination, boolean enemy) {
        super(map, position, 160);
        this.destination = destination;
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, this.threshold, this.threshold);
        this.enemy = enemy;

        if(enemy) {
            this.setSpeed(this.getSpeed() * 2);
        }
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.moveTowardDestination();

        boolean hit = false;

        Entity hitEntity = null;

        if(enemy) {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityPlayer) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        entity.setHealth(entity.getHealth() - 0.005f);
                        hit = true;
                        hitEntity = entity;
                    }
                }
            }
        } else {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityEnemy) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        entity.setHealth(entity.getHealth() - 0.1f);
                        hit = true;
                        hitEntity = entity;
                    }
                }
            }
        }

        if(hit) {
            EntityExplosion entityExplosion = new EntityExplosion(this.getMap(), this.getPosition(), hitEntity);
            this.getMap().spawnEntity(entityExplosion);

            this.getMap().despawnEntity(this);
        }

        if(this.getBody().overlaps(this.destinationBody)) {
            this.getMap().despawnEntity(this);
        }
    }

    private void moveTowardDestination() {
        Vector2 force = new Vector2();

        if(this.getPosition().x < this.destination.x) {
            force.add(this.getSpeed(), 0);
        }

        if(this.getPosition().x > this.destination.x) {
            force.add(-this.getSpeed(), 0);
        }

        if(this.getPosition().y < this.destination.y) {
            force.add(0, this.getSpeed());
        }

        if(this.getPosition().y > this.destination.y) {
            force.add(0, -this.getSpeed());
        }

        float delta = Gdx.graphics.getDeltaTime();

        Vector2 difference = new Vector2(this.getPosition().x - this.destination.x, this.getPosition().y - this.destination.y);

        difference.set(Math.abs(difference.x), Math.abs(difference.y));

        if(difference.x > difference.y) {
            float percentage = (difference.x) / difference.y;
            float percentageFloat = percentage / 100;

            force.y = force.y + force.y * percentageFloat;
            //System.out.println("P=" + percentage + " Modifying Y by " + percentageFloat + "x");
        }

        if(difference.y > difference.x) {
            float percentage = (difference.x) / difference.y;
            float percentageFloat = percentage / 100;

            force.x = force.x + force.x * percentageFloat;


            //System.out.println("P=" + percentage + " Modifying X by " + percentageFloat + "x");
        }

        if(Math.abs(difference.x) < this.threshold) {
            force.x = 0;
        }

        if(Math.abs(difference.y) < this.threshold) {
            force.y = 0;
        }

        float moveX = force.x * delta;
        float moveY = force.y * delta;

        //System.out.println("Moving by " + moveX + "/" + moveY);

        this.getPosition().add(force.x * delta, force.y * delta);
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

}
