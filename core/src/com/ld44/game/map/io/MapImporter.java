package com.ld44.game.map.io;

import com.badlogic.gdx.Gdx;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;
import com.ld44.game.map.MapDefinition;
import com.ld44.game.map.MapLayer;
import com.ld44.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapImporter {

    public static Map importMapFromFile(String path, MapDefinition mapDefinition, boolean internal) {
        String mapTileData = "";

        if(internal) {
            mapTileData = Gdx.files.internal(path).readString();
        } else {
            mapTileData = Gdx.files.external(path).readString();
        }

        String[] mapLayerData = mapTileData.split("\n");

        List<MapLayer> tileLayers = new ArrayList<MapLayer>();

        for(String mapLayer : mapLayerData) {
            List<TileType> layerTiles = new ArrayList<TileType>();

            String[] layerTileData = mapLayer.split(",");

            for(String layerTile : layerTileData) {
                int layerTileId = Integer.parseInt(layerTile);
                layerTiles.add(TileType.values()[layerTileId]);
            }

            MapLayer tileLayer = new MapLayer(layerTiles, mapDefinition);
            tileLayers.add(tileLayer);
        }

        return new Map(tileLayers, mapDefinition, new ArrayList<Entity>());
    }

}
