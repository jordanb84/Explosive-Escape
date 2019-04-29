package com.ld44.game.entity.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.map.Map;

public class EntityMediumEnemy extends EntityEnemy {

    public EntityMediumEnemy(Map map, Vector2 position) {
        super(5, map, position, 45, 52, 6, 30);;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(0.08f, "entity/enemy_medium_0.png", "entity/enemy_medium_1.png", "entity/enemy_medium_2.png", "entity/enemy_medium_3.png", "entity/enemy_medium_4.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        return directionalAnimation;
    }

    @Override
    public String[] getRipples() {
        return new String[] {"entity/water_ripple_small_000.png", "entity/water_ripple_small_001.png", "entity/water_ripple_small_002.png", "entity/water_ripple_small_003.png", "entity/water_ripple_small_004.png"};
    }

    @Override
    public Sprite getDestroyedSprite() {
        return Assets.getInstance().getSprite("entity/enemy_medium_destroyed.png");
    }

    @Override
    public void damage(float damage) {
        this.setHealth(this.getHealth() - damage / 8);
    }

    @Override
    public void fire(Entity entity) {
        EntityBullet bullet = new EntityBullet(this.getMap(), new Vector2(this.getPosition()), new Vector2(entity.getPosition()), true, "explosion/small/small_");
        EntityBullet bullet1 = new EntityBullet(this.getMap(), new Vector2(this.getPosition().x + 20, this.getPosition().y + 10), new Vector2(entity.getPosition()), true, "explosion/small/small_");

        this.getMap().spawnEntity(bullet);
        this.getMap().spawnEntity(bullet1);
    }

    public int getCratesMax() {
        return 8;
    }

    public int getCratesMin() {
        return 5;
    }

}
