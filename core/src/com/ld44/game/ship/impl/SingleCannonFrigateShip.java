package com.ld44.game.ship.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.impl.EntityBullet;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.ship.PlayerShip;

import java.util.ArrayList;
import java.util.List;

public class SingleCannonFrigateShip extends PlayerShip {

    public SingleCannonFrigateShip(Map map, EntityPlayer player) {
        super(map, player);
        //player.setSpeed(120);
        player.setMaxSpeed(160);
        player.setSpeedAcceleration(26);
    }

    @Override
    public List<EntityBullet> generateBulletsForPosition(Vector2 destination) {
        List<EntityBullet> bullets = new ArrayList<EntityBullet>();

        EntityBullet bullet = new EntityBullet(this.getMap(), this.getBulletOrigin(), destination, false, "explosion/small/small_");

        bullets.add(bullet);

        return bullets;
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(0.08f, "entity/small_0.png", "entity/small_1.png", "entity/small_2.png", "entity/small_3.png", "entity/small_4.png");

        DirectionalAnimation directionalAnimation = new DirectionalAnimation();
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.UP);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.DOWN);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.RIGHT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.LEFT);
        directionalAnimation.addAnimationForDirection(baseAnimation, Direction.NONE);

        return directionalAnimation;
    }

    @Override
    public float modifyDamage(float damage) {
        return damage;
    }

    @Override
    public Sprite getDestroyedSprite() {
        return Assets.getInstance().getSprite("entity/small_destroyed.png");
    }

}
