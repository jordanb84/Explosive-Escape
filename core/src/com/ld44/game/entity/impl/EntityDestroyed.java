package com.ld44.game.entity.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;

public class EntityDestroyed extends Entity {

    private Sprite sprite;

    public EntityDestroyed(Map map, Vector2 position, Sprite sprite, float rotation) {
        super(map, position, 0);
        this.sprite = sprite;
        this.sprite.setRotation(rotation);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.sprite.setPosition(this.getPosition().x, this.getPosition().y);
        this.sprite.draw(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {

    }

    @Override
    public DirectionalAnimation createAnimation() {
        return null;
    }

}
