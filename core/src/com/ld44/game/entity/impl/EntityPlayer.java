package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.map.Map;

public class EntityPlayer extends EntityBoat {

    public EntityPlayer(Map map, Vector2 position) {
        super(map, position, 30, 60, 4, 50);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);

      //  System.out.println(this.getSpeed());

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.changeDirection(Direction.UP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.changeDirection(Direction.DOWN);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.changeDirection(Direction.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.changeDirection(Direction.LEFT);
        }

        camera.position.add(this.getLastMovement().x, this.getLastMovement().y, 0);
        camera.update();
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(1, "entity/player.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        return directionalAnimation;
    }

}
