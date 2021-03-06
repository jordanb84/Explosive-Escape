package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityBoss extends EntityEnemy {

    private float burstElapsed;

    private float burstInterval = 0.4f;

    private Sprite bulletSprite;

    private Sprite bulletSpritePink;

    private List<IntervalBurst> bursts = new ArrayList<IntervalBurst>();

    private float rockeElapsed;

    private float defaultRocketInterval = 1;
    private float rocketInterval = defaultRocketInterval;

    private String rocketExplosion = ("explosion/large/rocket_");

    private OrthographicCamera camera;

    public EntityBoss(Map map, Vector2 position) {
        super(1000, map, position, 60, 70, 4, 50);
        this.bulletSprite = Assets.getInstance().getSprite("entity/basicBullet.png");
        this.bulletSpritePink = Assets.getInstance().getSprite("entity/basicBulletPink.png");

        this.bursts.add(new IntervalBurst(1.5f,"explosion/medium/medium_",5, 6, this, 0.3f, new Vector2(100, 50), 100, this.bulletSprite));
        this.bursts.add(new IntervalBurst(1.5f,"explosion/nine/nine_", 3, 3, this, 2, new Vector2(200, 100), 200, this.bulletSpritePink));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.camera = camera;

        for(IntervalBurst burst : this.bursts) {
            burst.update();
        }

        this.rockeElapsed += 1 * Gdx.graphics.getDeltaTime();

        if(this.rockeElapsed >= this.rocketInterval) {
            EntityPlayer player = this.getMap().getPlayer();

            Vector2 origin = new Vector2(this.getPosition().x, this.getPosition().y);
            Vector2 destination = new Vector2(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2);

            EntityRocket rocket = new EntityRocket(this.getMap(), origin, destination, true, this.rocketExplosion);

            this.getMap().spawnEntity(rocket);

            this.rockeElapsed = 0;

            this.rocketInterval = new Random().nextInt((int) this.defaultRocketInterval);

            if(this.rocketInterval < this.defaultRocketInterval / 2) {
                rocketInterval = this.defaultRocketInterval;
            }
        }
    }

    public EntityBulletBurst generateBurst(float damageMultiplier, String explosion, int rows, int columns, Vector2 spread, float speed, Sprite sprite) {
        EntityPlayer player = this.getMap().getPlayer();

        Vector2 origin = new Vector2(this.getPosition().x, this.getPosition().y);
        Vector2 destination = new Vector2(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2);

        EntityBulletBurst burst = new EntityBulletBurst(damageMultiplier, explosion, rows, columns, this.getMap(), origin, destination, spread, speed, sprite);

        return burst;
    }

    @Override
    public Sprite getDestroyedSprite() {
        return Assets.getInstance().getSprite("entity/large_destroyed.png");
    }

    @Override
    public DirectionalAnimation createAnimation() {
        Animation baseAnimation = new Animation();

        baseAnimation.addFrames(0.08f, "entity/large_gunned_0.png", "entity/large_gunned_1.png", "entity/large_gunned_2.png", "entity/large_gunned_3.png", "entity/large_gunned_4.png");

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
    public float getRotationModifier() {
        return this.getRotationSpeed();
    }

    public List<EntityBullet> generateBulletBall(Vector2 origin, Vector2 destination) {
        List<EntityBullet> bullets = new ArrayList<EntityBullet>();

        Random random = new Random();

        for(int row = 0; row < 5; row++) {
            for(int column = 0; column < 6; column++) {
                Vector2 offset = new Vector2(random.nextInt(60), random.nextInt(30));

                Vector2 position = new Vector2(origin.x + offset.x, origin.y + offset.y);

                EntityBullet bullet = new EntityBullet(this.getMap(), position, destination, true, "explosion/small/small_");

                bullets.add(bullet);
            }
        }

        return bullets;
    }

    @Override
    public void damage(float amount) {
        //System.out.println("Old hp " + this.getHealth());
        this.setHealth(this.getHealth() - 0.0007f);
        //System.out.println("New hp " + this.getHealth());
    }

    @Override
    public void updateFireRange(OrthographicCamera camera) {
        this.getFireRange().set(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
    }

    @Override
    public void die() {
        super.die();
        Vector2 pos = new Vector2(this.getPolygonBody().getX(), this.getPolygonBody().getY());
        this.getMap().win(new Vector2(pos.x - this.getWidth() / 2, pos.y - this.getHeight() / 2), this.camera);
    }

}

class IntervalBurst {

    private EntityBoss boss;

    private float burstElapsed = 0;

    private float burstInterval;

    private Vector2 spread;

    private float speed;

    private float minimumInterval;

    private Sprite sprite;

    private int rows;

    private int columns;

    private String explosion;

    private float damageMultiplier;

    public IntervalBurst(float damageMultiplier, String explosion, int rows, int columns, EntityBoss boss, float interval, Vector2 spread, float speed, Sprite sprite) {
        this.boss = boss;
        this.burstInterval = interval;
        this.spread = spread;
        this.speed = speed;
        this.minimumInterval = interval / 2;
        this.sprite = sprite;
        this.rows = rows;
        this.columns = columns;
        this.explosion = explosion;
        this.damageMultiplier = damageMultiplier;
    }

    public void update() {
        this.burstElapsed += 1 * Gdx.graphics.getDeltaTime();

        if(this.burstElapsed >= this.burstInterval) {
            this.burstElapsed = 0;
            this.boss.getMap().spawnEntity(this.boss.generateBurst(this.damageMultiplier, this.explosion, this.rows, this.columns, this.spread, this.speed, this.sprite));

            this.burstInterval = new Random().nextFloat() * 5;

            if(this.burstInterval < this.minimumInterval) {
                this.burstInterval = this.minimumInterval;
            }
        }
    }

}