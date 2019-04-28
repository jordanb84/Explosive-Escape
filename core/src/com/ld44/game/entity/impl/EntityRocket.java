package com.ld44.game.entity.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.animation.Frame;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.map.Map;

public class EntityRocket extends EntityBullet {

    public EntityRocket(Map map, Vector2 position, Vector2 destination, boolean enemy, String explosionSpritePath) {
        super(map, position, destination, enemy, explosionSpritePath);
        Animation baseAnimation = new Animation();

        float duration = 0.03f;

        for(int i = 0; i < 15; i++) {
            baseAnimation.addFrame(new Frame(Assets.getInstance().getSprite("explosion/rocket/rocket_" + i + ".png"), duration));
        }

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        this.setAnimation(directionalAnimation);

        this.setLifespan(3);
    }

    @Override
    public DirectionalAnimation createAnimation() {
        return super.createAnimation();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.setRotation(this.rotationToPoint(new Vector2(this.getMap().getPlayer().getPosition())));

        this.getActiveSprite().setRotation(this.getRotation());
        this.getAnimation().render(batch, this.getPosition(), this.getDirection());
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.applyVelocity();

        if(this.getRotation() >= 360) {
            this.setRotation(0);
        }

        this.getAnimation().update(Direction.UP);
    }

    @Override
    public void moveTowardDestination() {

    }

}
