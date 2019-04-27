package com.ld44.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Entity;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.map.MapLayer;

public class MiniMap {

    private Map map;

    private Sprite radar;

    private OrthographicCamera radarCamera;

    public MiniMap(Map map) {
        this.map = map;
        this.radar = Assets.getInstance().getSprite("editorBackground.png");
        this.radar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.radar.setAlpha(0.3f);

        this.radarCamera = new OrthographicCamera();
        this.radarCamera.setToOrtho(false, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(this.radarCamera.combined);
        float scale = 0.2f;
        float alpha = 0.5f;
        Vector2 offset = new Vector2( 250, 0);

        //Vector2 center = new Vector2(-camera.position.x * scale / 4, -camera.position.y * scale / 4);
        Vector2 center = new Vector2();

        for(MapLayer tileLayer : this.map.getTileLayers()) {
            tileLayer.render(batch, scale, alpha, new Vector2(offset.x + center.x, offset.y + center.y));
        }

        for(Entity entity : this.map.getEntities()) {
            float spriteScale = scale;
            entity.getActiveSprite().setScale(spriteScale);

            System.out.println("Drawing at scale " + spriteScale);

            float smallScale = scale;
            Vector2 position = new Vector2(entity.getPosition().x * smallScale, entity.getPosition().y * smallScale);
            position.add(offset.x + center.x, offset.y + center.y);

            if(!(entity instanceof EntityPlayer)) {
                entity.getActiveSprite().setAlpha(alpha);
            }

            entity.getActiveSprite().setPosition(position.x + 160, position.y + 160);
            entity.getActiveSprite().draw(batch);
            entity.getActiveSprite().setScale(1);
            entity.getActiveSprite().setAlpha(1);
        }

        int mapPixelWidth = this.map.getMapDefinition().getMapWidth() * this.map.getMapDefinition().getTileWidth();
        int mapPixelHeight = this.map.getMapDefinition().getMapHeight() * this.map.getMapDefinition().getTileHeight();
        this.radar.setSize(mapPixelWidth * scale * 1.2f / 4, mapPixelHeight * scale * 1.2f);

        this.radar.setPosition(offset.x * scale - 45, offset.y * scale - 32);
      //  this.radar.draw(batch);
        batch.setProjectionMatrix(camera.combined);
    }

    public void update(OrthographicCamera camera) {

    }

}
