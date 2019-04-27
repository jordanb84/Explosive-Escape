package com.ld44.game.ship;

import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.impl.EntityBullet;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;

import java.util.List;

public abstract class PlayerShip {

    private Map map;

    private EntityPlayer player;

    private DirectionalAnimation animation;

    public PlayerShip(Map map, EntityPlayer player) {
        this.map = map;
        this.player = player;
        this.animation = this.createAnimation();
        this.player.setHealth(1);
    }

    public void fire(Vector2 destination) {
        for(EntityBullet bullet : this.generateBulletsForPosition(destination)) {
            this.map.spawnEntity(bullet);
        }
    }

    public abstract List<EntityBullet> generateBulletsForPosition(Vector2 position);

    public Map getMap() {
        return map;
    }

    public Vector2 getBulletOrigin() {
        return new Vector2(this.player.getPosition().x, this.player.getPosition().y + this.player.getHeight() / 2);
    }

    public abstract DirectionalAnimation createAnimation();

    public DirectionalAnimation getAnimation() {
        return animation;
    }

    public abstract float modifyDamage(float damage);

}
