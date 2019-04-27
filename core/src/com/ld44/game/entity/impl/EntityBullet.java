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

    public EntityBullet(Map map, Vector2 position, Vector2 destination, boolean enemy) {
        super(map, position, 160);
        this.destination = destination;
        this.destinationBody = new Rectangle(this.destination.x, this.destination.y, 0, 0);
        this.enemy = enemy;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.moveTowardDestination();

        if(enemy) {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityPlayer) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        entity.setHealth(entity.getHealth() - 0.01f);
                        this.getMap().despawnEntity(this);
                    }
                }
            }
        } else {
            for(Entity entity : this.getMap().getEntities()) {
                if(entity instanceof EntityEnemy) {
                    if(entity.getBody().overlaps(this.getBody())) {
                        entity.setHealth(entity.getHealth() - 0.1f);
                        this.getMap().despawnEntity(this);
                    }
                }
            }
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
