package com.ld44.game.ship.impl;

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

public class DoubleCannonDestroyerShip extends PlayerShip {

    public DoubleCannonDestroyerShip(Map map, EntityPlayer player) {
        super(map, player);
        player.setSpeed(180);
        player.setMaxSpeed(210);
        player.setSpeedAcceleration(16);
    }

    @Override
    public List<EntityBullet> generateBulletsForPosition(Vector2 destination) {
        List<EntityBullet> bullets = new ArrayList<EntityBullet>();

        EntityBullet bullet = new EntityBullet(this.getMap(), this.getBulletOrigin(), destination, false, "explosion/small/small_");
        EntityBullet bullet1 = new EntityBullet(this.getMap(), new Vector2(this.getBulletOrigin().x + 20, this.getBulletOrigin().y + 10), destination, false, "explosion/small/small_");

        EntityBullet bullet2 = new EntityBullet(this.getMap(), this.getBulletOrigin(), destination, false, "explosion/small/small_");
        EntityBullet bullet3 = new EntityBullet(this.getMap(), new Vector2(this.getBulletOrigin().x + 20, this.getBulletOrigin().y + 10), destination, false, "explosion/small/small_");

        bullet2.setSprite(Assets.getInstance().getSprite("entity/basicBulletGreen.png"));
        bullet3.setSprite(Assets.getInstance().getSprite("entity/basicBulletGreen.png"));

        bullet2.setSpeed(bullet2.getSpeed() * 1.5f);
        bullet3.setSpeed(bullet2.getSpeed() * 1.5f);

        bullets.add(bullet);
        bullets.add(bullet1);
        bullets.add(bullet2);
        bullets.add(bullet3);


        return bullets;
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(0.08f, "entity/medium_large_0.png", "entity/medium_large_1.png", "entity/medium_large_2.png", "entity/medium_large_3.png", "entity/medium_large_4.png");

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
