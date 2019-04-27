package com.ld44.game.ship.impl;

import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.impl.EntityBullet;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.ship.PlayerShip;

import java.util.ArrayList;
import java.util.List;

public class DoubleCannonDestroyerShip extends PlayerShip {

    public DoubleCannonDestroyerShip(Map map, EntityPlayer player) {
        super(map, player);
    }

    @Override
    public List<EntityBullet> generateBulletsForPosition(Vector2 destination) {
        List<EntityBullet> bullets = new ArrayList<EntityBullet>();

        EntityBullet bullet = new EntityBullet(this.getMap(), this.getBulletOrigin(), destination, false);
        EntityBullet bullet1 = new EntityBullet(this.getMap(), new Vector2(this.getBulletOrigin().x + 20, this.getBulletOrigin().y + 10), destination, false);

        bullets.add(bullet);
        bullets.add(bullet1);

        return bullets;
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(0.08f, "entity/small_double_0.png", "entity/small_double_1.png", "entity/small_double_2.png", "entity/small_double_3.png", "entity/small_double_4.png");

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
        return damage / 2;
    }

}
