package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.animation.Frame;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;

import java.util.Random;

public class EntityCrate extends Entity {

    private boolean growing;

    private float growElapsed;

    private float scale;

    //go to player if they're near

    private Rectangle grabBody;

    private int value;

    public EntityCrate(Map map, Vector2 position) {
        super(map, position, 80);
        this.growing = true;
        this.scale = 1;
        this.grabBody = new Rectangle();

        this.value = new Random().nextInt(5);

        if(value < 3) {
            value = 3;
        }
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        float changeRate = 1 * Gdx.graphics.getDeltaTime() / 2;

        if(this.growing) {
            this.scale = this.scale + changeRate;

            this.getActiveSprite().setScale(this.scale);

            if(this.scale >= 1.2f) {
                this.growing = false;
            }
        } else {
            this.scale = this.scale - changeRate;

            this.getActiveSprite().setScale(this.scale);

            if(this.scale <= 0.8f) {
                this.growing = true;
            }
        }

        this.grabBody.set(this.getPosition().x - this.getWidth() / 2, this.getPosition().y - this.getHeight() / 2, this.getWidth() * 2, this.getHeight() * 2);

        try {
            EntityPlayer player = this.getMap().getPlayer();

            if (this.grabBody.overlaps(player.getPolygonBody().getBoundingRectangle())) {
                this.moveTowardDestination(new Vector2(player.getPosition()), 10);
            }

            if (this.getBody().overlaps(player.getPolygonBody().getBoundingRectangle())) {
                this.getMap().getHud().modifyCash(this.value);
                this.getMap().despawnEntity(this);
            }
        } catch(ArrayIndexOutOfBoundsException playerDead) {

        }
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrame(new Frame(Assets.getInstance().getSprite("entity/box_small.png"), 1));

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        return directionalAnimation;
    }

}
