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
        int index = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int rowTile = 0; rowTile < this.mapDefinition.getMapWidth(); rowTile++) {
                Vector2 position = new Vector2(rowTile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());
                //Tile tile = this.tiles.get(index).TILE;

                //tile.render(batch, position);

                TileType type = this.tiles.get(index);
                type.SPRITE.setPosition(position.x, position.y);
                type.SPRITE.draw(batch);
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
