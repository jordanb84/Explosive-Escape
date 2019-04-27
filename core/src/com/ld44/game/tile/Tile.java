package com.ld44.game.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tile {

    private TileType tileType;

    public Tile(TileType tileType) {
        this.tileType = tileType;
    }

    public void render(SpriteBatch batch, Vector2 position) {
        this.tileType.SPRITE.setPosition(position.x, position.y);
        this.tileType.SPRITE.draw(batch);
    }

    public void update(Vector2 position) {

    }

    public Rectangle getBody(Vector2 position) {
        return new Rectangle(position.x, position.y, this.tileType.SPRITE.getWidth(), this.tileType.SPRITE.getHeight());
    }

    public TileType getTileType() {
        return tileType;
    }

}
