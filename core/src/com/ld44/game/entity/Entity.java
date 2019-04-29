package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;

public abstract class Entity {

    private Vector2 position;

    private float speed;

    private DirectionalAnimation animation;

    private Rectangle body = new Rectangle();

    private Direction direction;

    private Vector2 lastMovement = new Vector2();

    private Map map;

    private float health;

    private float rotation;

    public Entity(Map map, Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
        this.animation = this.createAnimation();
        this.direction = Direction.UP;
        this.map = map;
        this.health = 1;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getAnimation().render(batch, this.getPosition(), this.getDirection());
    }

    public void update(OrthographicCamera camera) {
        this.updateBody();
        this.getLastMovement().set(0, 0);

        if(this.getHealth() <= 0.2f) {
            this.die();
            this.getMap().despawnEntity(this);
        }
    }

    public void updateBody() {
        this.getBody().set(this.getPosition().x, this.getPosition().y, this.getWidth(), this.getHeight());
    }

    public abstract DirectionalAnimation createAnimation();

    public boolean move(Direction direction) {
        Vector2 force = new Vector2();

        switch(direction) {
            case UP:
                force.set(0, this.getSpeed() * Gdx.graphics.getDeltaTime());
                break;
            case DOWN:
                force.set(0, -this.getSpeed() * Gdx.graphics.getDeltaTime());
                break;
            case RIGHT:
                force.set(this.getSpeed() * Gdx.graphics.getDeltaTime(), 0);
                break;
            case LEFT:
                force.set(-this.getSpeed() * Gdx.graphics.getDeltaTime(), 0);
                break;
        }

        this.getPosition().add(force);
        this.setDirection(direction);

        return true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return this.getAnimation().getWidth(this.getDirection());
    }

    public float getHeight() {
        return this.getAnimation().getHeight(this.getDirection());
    }

    public float getSpeed() {
        return speed;
    }

    public DirectionalAnimation getAnimation() {
        return animation;
    }

    public Rectangle getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Animation getActiveAnimation() {
        return this.getAnimation().getAnimationForDirection(this.getDirection());
    }

    public Sprite getActiveSprite() {
        return this.getActiveAnimation().getCurrentFrame().getSprite();
    }

    public void changeSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getLastMovement() {
        return lastMovement;
    }

    public Map getMap() {
        return map;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void reset() {
        this.health = 1;
    }

    public void die() {

    }

    public void setAnimation(DirectionalAnimation animation) {
        this.animation = animation;
    }

    public float rotationToPoint(Vector2 target) {
        float pointRotation = 0;

        Vector2 facing = new Vector2(target.x - this.getPosition().x, target.y - this.getPosition().y);

        pointRotation = (float) Math.toDegrees((Math.atan2(facing.y, facing.x)));
        pointRotation = pointRotation + (360 / 4);

        pointRotation = pointRotation + 180;

        return pointRotation;
    }

    public void applyVelocity() {
        float forceX = -this.getSpeed() * (float) Math.cos(Math.toRadians(this.getRotation() - 90));
        float forceY = -this.getSpeed() * (float) Math.sin(Math.toRadians(this.getRotation() - 90));

        float delta = Gdx.graphics.getDeltaTime();

        this.getPosition().add(forceX * delta, forceY * delta);
        this.getLastMovement().set(forceX * delta, forceY * delta);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }


    public void moveTowardDestination(Vector2 destination, float threshold) {
        Vector2 force = new Vector2();

        if(this.getPosition().x < destination.x) {
            force.add(this.getSpeed(), 0);
        }

        if(this.getPosition().x > destination.x) {
            force.add(-this.getSpeed(), 0);
        }

        if(this.getPosition().y < destination.y) {
            force.add(0, this.getSpeed());
        }

        if(this.getPosition().y > destination.y) {
            force.add(0, -this.getSpeed());
        }

        float delta = Gdx.graphics.getDeltaTime();

        Vector2 difference = new Vector2(this.getPosition().x - destination.x, this.getPosition().y - destination.y);

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

        if(Math.abs(difference.x) < threshold) {
            force.x = 0;
        }

        if(Math.abs(difference.y) < threshold) {
            force.y = 0;
        }

        float moveX = force.x * delta;
        float moveY = force.y * delta;

        //System.out.println("Moving by " + moveX + "/" + moveY);

        this.getPosition().add(force.x * delta, force.y * delta);
    }

}
