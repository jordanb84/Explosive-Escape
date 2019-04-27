package com.ld44.game.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.entity.Direction;

import java.util.HashMap;

public class DirectionalAnimation {

    private HashMap<Direction, Animation> animations = new HashMap<Direction, Animation>();

    public DirectionalAnimation() {

    }

    public DirectionalAnimation(Animation upAnimation, Animation downAnimation, Animation rightAnimation, Animation leftAnimation) {
        this.animations.put(Direction.UP, upAnimation);
        this.animations.put(Direction.DOWN, downAnimation);
        this.animations.put(Direction.RIGHT, rightAnimation);
        this.animations.put(Direction.LEFT, leftAnimation);
    }

    public void render(SpriteBatch batch, Vector2 position, Direction direction) {
        this.animations.get(direction).render(batch, position);
    }

    public void update(Direction direction) {
        this.animations.get(direction).update();
    }

    public void addAnimationForDirection(Animation animation, Direction direction) {
        this.animations.put(direction, animation);
    }

    public float getWidth(Direction direction) {
        return this.animations.get(direction).getCurrentWidth();
    }

    public float getHeight(Direction direction) {
        return this.animations.get(direction).getCurrentHeight();
    }

    public Animation getAnimationForDirection(Direction direction) {
        return this.animations.get(direction);
    }

}
