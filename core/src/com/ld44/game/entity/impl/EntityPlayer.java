package com.ld44.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld44.game.animation.Animation;
import com.ld44.game.animation.DirectionalAnimation;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Direction;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.entity.EntityEnemy;
import com.ld44.game.map.Map;
import com.ld44.game.ship.PlayerShip;
import com.ld44.game.ship.impl.DoubleCannonDestroyerShip;
import com.ld44.game.ship.impl.DoubleCannonFrigateShip;
import com.ld44.game.ship.impl.SingleCannonFrigateShip;

public class EntityPlayer extends EntityBoat {

    private Sprite crosshair;

    private PlayerShip playerShip;

    private float fireInterval = 0.1f;

    private float fireElapsed = fireInterval;

    public EntityPlayer(Map map, Vector2 position) {
        super(map, position, 120, 150, 8, 50);
        this.setPlayerShip(new SingleCannonFrigateShip(map, this));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.getActiveSprite().setAlpha(this.getHealth());
        this.getActiveSprite().setPosition(this.getPosition().x, this.getPosition().y);
        this.getActiveSprite().setRotation(this.getRotation());
        this.getActiveSprite().draw(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.fireElapsed += 1 * Gdx.graphics.getDeltaTime();

        int[] fireKeys = {Input.Keys.SPACE, Input.Keys.E, Input.Keys.F, Input.Keys.J, Input.Keys.K, Input.Keys.R};

        boolean firing = false;

        boolean fireQuick = false;

        for(int fireKey : fireKeys) {
            if(Gdx.input.isKeyPressed(fireKey)) {
                firing = true;
            }

            if(Gdx.input.isKeyJustPressed(fireKey)) {
                fireQuick = true;
            }
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            firing = true;
        }

        if(firing) {
            if(this.fireElapsed >= this.fireInterval) {
                Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePosition);

                this.playerShip.fire(new Vector2(mousePosition.x, mousePosition.y));

                this.fireElapsed = 0;
            }
        }

        if(fireQuick) {
            this.fireElapsed = this.fireInterval;
        }

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        //this.pointAt(new Vector2(mousePosition.x, mousePosition.y));

        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.changeDirection(Direction.UP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.changeDirection(Direction.DOWN);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.changeDirection(Direction.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.changeDirection(Direction.LEFT);
        }

        camera.position.set(this.getPosition().x + this.getWidth() / 2, this.getPosition().y + this.getHeight() / 2, 0);
        camera.update();
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
    public String[] getRipples() {
       // return new String[] {"entity/water_ripple_small_000.png", "entity/water_ripple_small_001.png", "entity/water_ripple_small_002.png", "entity/water_ripple_small_003.png", "entity/water_ripple_small_004.png"};
        return new String[] {"entity/water_ripple_small_000.png"};
    }

    public void setPlayerShip(PlayerShip playerShip) {
        this.setAnimation(playerShip.getAnimation());
        this.playerShip = playerShip;
    }

    public void damage(float amount) {
        this.setHealth(this.getHealth() - this.playerShip.modifyDamage(amount));
    }

    @Override
    public boolean canMove() {
        /**for(Entity entity : this.getMap().getEntities()) {
            if(entity instanceof EntityBoss) {
                EntityEnemy enemy = ((EntityEnemy) entity);
                enemy.updatePolygonBody();
                System.out.println(enemy.getPolygonBody().getBoundingRectangle().width);
                if(this.getPolygonBody().getBoundingRectangle().overlaps(enemy.getPolygonBody().getBoundingRectangle())) {
                    return false;
                }
            }
        }**/

        return true;
    }

    @Override
    public void die() {
        super.die();
        this.getMap().reset();
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

}
