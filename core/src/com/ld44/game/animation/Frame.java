package com.ld44.game.animation;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Frame {

    private Sprite sprite;

    private float duration;

    public Frame(Sprite sprite, float duration) {
        this.sprite = sprite;
        this.duration = duration;
    }

    public void render(SpriteBatch batch, Vector2 position) {
        this.sprite.setPosition(position.x, position.y);
        this.sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getDuration() {
        return duration;
    }

}