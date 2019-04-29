package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.animation.Frame;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;

public class EntityExplosion extends Entity {

    private Entity hitEntity;

    private float delay;

    private boolean hasDelay;

    private float delayElapsed;

    public EntityExplosion(Map map, Vector2 position, Entity hitEntity, String spritePath) {
        super(map, position, 0);
        this.hitEntity = hitEntity;

        Animation baseAnimation = new Animation();

        float duration = 0.03f;

        for(int i = 0; i < 23; i++) {
            //baseAnimation.addFrame(new Frame(Assets.getInstance().getSprite("explosion/small/small_" + i + ".png"), duration));
            baseAnimation.addFrame(new Frame(Assets.getInstance().getSprite(spritePath + i + ".png"), duration));
        }

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        this.setAnimation(directionalAnimation);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(!this.hasDelay) {
            this.getActiveSprite().setPosition(this.getPosition().x, this.getPosition().y);
            this.getActiveSprite().setAlpha(0.8f);
            this.getActiveSprite().draw(batch);
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        //this.getActiveSprite().setRotation(this.hitEntity.getActiveSprite().getRotation());
        if(this.hasDelay) {
            this.delayElapsed += 1 * Gdx.graphics.getDeltaTime();

            if(this.delayElapsed >= this.delay) {
                this.hasDelay = false;
            }
        } else {
            this.getActiveAnimation().update();

            if(this.getActiveAnimation().isFinished()) {
                this.getMap().despawnEntity(this);
            }

            if(this.hitEntity != null) {
                this.getPosition().add(this.hitEntity.getLastMovement().x, this.hitEntity.getLastMovement().y);
            }
        }

    }

    @Override
    public DirectionalAnimation createAnimation() {
        return null;
    }

    public void setDelay(float delay) {
        this.delay = delay;
        this.hasDelay = true;
    }

}
