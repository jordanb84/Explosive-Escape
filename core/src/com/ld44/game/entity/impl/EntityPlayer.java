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
import com.ld44.game.entity.EntityBoat;
import com.ld44.game.map.Map;
import com.ld44.game.ship.PlayerShip;
import com.ld44.game.ship.impl.SingleCannonFrigateShip;

public class EntityPlayer extends EntityBoat {

    private Sprite crosshair;

    private PlayerShip playerShip;

    public EntityPlayer(Map map, Vector2 position) {
        super(map, position, 120, 150, 8, 50);
        this.playerShip = new SingleCannonFrigateShip(map, this);
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

      //  System.out.println(this.getSpeed());


        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition);

            this.playerShip.fire(new Vector2(mousePosition.x, mousePosition.y));
        }

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        //this.pointAt(new Vector2(mousePosition.x, mousePosition.y));

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.changeDirection(Direction.UP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.changeDirection(Direction.DOWN);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.changeDirection(Direction.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
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

}
