package com.ld44.game.map;

public class MapDefinition {

    protected final int MAP_WIDTH;
    protected final int MAP_HEIGHT;

    protected final int TILE_WIDTH;
    protected final int TILE_HEIGHT;

    protected final int LAYERS;

    public MapDefinition(int mapWidth, int mapHeight, int tileWidth, int tileHeight, int layers) {
        this.MAP_WIDTH = mapWidth;
        this.MAP_HEIGHT = mapHeight;

        this.TILE_WIDTH = tileWidth;
        this.TILE_HEIGHT = tileHeight;

        this.LAYERS = layers;
    }

    public int getLayers() {
        return this.LAYERS;
    }

    public int getMapWidth() {
        return MAP_WIDTH;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    public int getTileWidth() {
        return TILE_WIDTH;
    }

    public int getTileHeight() {
        return TILE_HEIGHT;
    }

}