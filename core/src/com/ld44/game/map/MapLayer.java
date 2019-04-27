package com.ld44.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld44.game.tile.Tile;
import com.ld44.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapLayer {

    private List<TileType> tiles = new ArrayList<TileType>();

    private MapDefinition mapDefinition;

    public MapLayer(List<TileType> tiles, MapDefinition mapDefinition) {
        this.tiles = tiles;
        this.mapDefinition = mapDefinition;
    }

    public MapLayer(TileType groundType, MapDefinition mapDefinition) {
        this.mapDefinition = mapDefinition;
        this.tiles = this.generateLayer(groundType);
    }

    public void render(SpriteBatch batch) {
        this.render(batch, 1, 1, new Vector2(0, 0));
    }

    public void render(SpriteBatch batch, float scale, float alpha, Vector2 offset) {
        int index = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int rowTile = 0; rowTile < this.mapDefinition.getMapWidth(); rowTile++) {
                Vector2 position = new Vector2(rowTile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                position.set(position.x * scale, position.y * scale);
                position.add(offset.x, offset.y);

                TileType type = this.tiles.get(index);
                //type.SPRITE.setPosition(position.x * scale + offset.x, position.y * scale + offset.y);
                type.SPRITE.setScale(scale);
                type.SPRITE.setAlpha(alpha);
                type.SPRITE.setPosition(position.x, position.y);
                type.SPRITE.draw(batch);
                type.SPRITE.setScale(1);
                type.SPRITE.setAlpha(1);
                index++;
            }
        }
    }

    public void update() {
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int rowTile = 0; rowTile < this.mapDefinition.getMapWidth(); rowTile++) {
                Vector2 position = new Vector2(rowTile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());
                Tile tile = this.tiles.get(row * this.mapDefinition.getMapHeight() + rowTile).TILE;

                tile.update(position);
            }
        }
    }

    private List<TileType> generateLayer(TileType fillType) {
        List<TileType> layer = new ArrayList<TileType>();
        for(int tile = 0; tile < this.mapDefinition.MAP_HEIGHT * this.mapDefinition.MAP_WIDTH; tile++) {
            layer.add(fillType);
        }

        return layer;
    }

    public List<TileType> getTiles() {
        return tiles;
    }

    public void setTile(int tileIndex, TileType tileType) {
        this.getTiles().set(tileIndex, tileType);
    }

}
