package com.ld44.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.entity.impl.EntityBullet;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;

public abstract class EntityBoat extends Entity {

    private float maxSpeed;

    private float speedAcceleration;

    private float rotationSpeed;

    private Animation rippleAnimation;

    private Polygon polygonBody = new Polygon();

    public EntityBoat(Map map, Vector2 position, float speed, float maxSpeed, float speedAcceleration, float rotationSpeed) {
        super(map, position, speed);
        this.maxSpeed = maxSpeed;
        this.speedAcceleration = speedAcceleration;
        this.rotationSpeed = rotationSpeed;

        this.rippleAnimation = new Animation();

        rippleAnimation.addFrames(0.1f, this.getRipples());
    }


    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.renderRipple(batch);
        this.getActiveSprite().setOrigin(0, 0);
        this.getActiveSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getActiveSprite().setRotation(this.getRotation());
        this.getActiveSprite().draw(batch);
    }

    public void renderRipple(SpriteBatch batch) {
        //this.rippleAnimation.getCurrentFrame().getSprite().setSize(54, 105);
        this.rippleAnimation.getCurrentFrame().getSprite().setOrigin(0, 0);
        this.rippleAnimation.getCurrentFrame().getSprite().setRotation(this.getRotation());
        this.rippleAnimation.render(batch, new Vector2(this.getPosition().x - this.getWidth() / 16, this.getPosition().y - this.getHeight() / 2));
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.updatePolygonBody();

        this.applyVelocity();

        if(this.getRotation() >= 360) {
            this.setRotation(0);
        }

        this.getActiveAnimation().update();
        this.rippleAnimation.update();
    }

    public void applyVelocity() {
        if(this.canMove()) {
            float forceX = -this.getSpeed() * (float) Math.cos(Math.toRadians(this.getRotation() - 90));
            float forceY = -this.getSpeed() * (float) Math.sin(Math.toRadians(this.getRotation() - 90));

            if(this.getPosition().y > 7243 && forceY > 0) {
                forceY = 0;
            }

            if(this.getPosition().y < 200 && forceY < 0) {
                forceY = 0;
            }

            if(this.getPosition().x > 9700 && forceX > 0) {
                forceX = 0;
            }

            if(this.getPosition().x < 400 && forceX < 0) {
                forceX = 0;
            }

            float delta = Gdx.graphics.getDeltaTime();

            this.getPosition().add(forceX * delta, forceY * delta);
            this.getLastMovement().set(forceX * delta, forceY * delta);
        }
    }

    public boolean canMove() {
        return true;
    }

    public void changeDirection(Direction direction) {
        float delta = Gdx.graphics.getDeltaTime();

        float speedModifier = 0;

        float rotationChange = this.getRotation();

        switch (direction) {
            case UP:
                speedModifier = this.speedAcceleration * delta;
                break;
            case DOWN:
                float stopModifier = 6;
                speedModifier = -this.speedAcceleration * delta * stopModifier;
                break;
            case RIGHT:
                //this.setRotation(this.getRotation() - this.getManeuverability() * Gdx.graphics.getDeltaTime());
                rotationChange = this.getRotation() - this.getRotationSpeed() * delta;
                break;
            case LEFT:
                rotationChange = this.getRotation() + this.getRotationSpeed() * delta;
                break;
            case NONE:
                this.changeSpeed(0);
                break;
        }

        this.setDirection(direction);

        this.changeSpeed(this.getSpeed() + speedModifier);

        this.setRotation(rotationChange);

        if(this.getSpeed() >= this.maxSpeed) {
            this.changeSpeed(this.maxSpeed);
        }

        if(this.getSpeed() < 0) {
            this.changeSpeed(0);
        }
    }


    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void pointAt(Vector2 target) {
        this.setRotation(this.rotationToPoint(target));
    }

    public float rotationToPoint(Vector2 target) {
        float pointRotation = 0;

        Vector2 facing = new Vector2(target.x - this.getPosition().x, target.y - this.getPosition().y);

        pointRotation = (float) Math.toDegrees((Math.atan2(facing.y, facing.x)));
        pointRotation = pointRotation + (360 / 4);

        pointRotation = pointRotation + 180;

        return pointRotation;
    }

    public abstract String[] getRipples();

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setSpeedAcceleration(float speedAcceleration) {
        this.speedAcceleration = speedAcceleration;
    }

    public Polygon getPolygonBody() {
        return polygonBody;
    }

    public void updatePolygonBody() {
        this.getPolygonBody().setPosition(this.getPosition().x, this.getPosition().y);
        this.getPolygonBody().setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.getPolygonBody().setVertices(new float[]{0,0,this.getWidth(),0,this.getWidth(),this.getHeight(),0,this.getHeight()});
        this.getPolygonBody().setRotation(this.getRotation());
    }

    public void damage(float damage) {
        this.setHealth(this.getHealth() - damage);
    }

}
