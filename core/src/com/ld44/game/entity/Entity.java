package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
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

        if(this.getHealth() <= 0) {
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

}
