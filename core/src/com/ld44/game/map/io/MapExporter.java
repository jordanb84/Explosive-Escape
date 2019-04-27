package com.ld44.game.map.io;

import com.badlogic.gdx.Gdx;
import com.ld44.game.map.Map;
import com.ld44.game.map.MapLayer;
import com.ld44.game.tile.TileType;

public class MapExporter {

    public static void exportMapToFile(Map map, String path) {
        System.out.println("Exporting map");
        String mapTileData = "";

        for(int mapTileLayerIndex = 0; mapTileLayerIndex < map.getTileLayers().size(); mapTileLayerIndex++) {
            MapLayer tileLayer = map.getTileLayers().get(mapTileLayerIndex);

            for(int tileIndex = 0; tileIndex < tileLayer.getTiles().size(); tileIndex++) {
                TileType tile = tileLayer.getTiles().get(tileIndex);

                mapTileData += tile.ordinal() + ",";
            }

            mapTileData += "\n";

            System.out.println("Layer " + mapTileLayerIndex);
        }

        System.out.println("Writing");
        Gdx.files.local(path).writeString(mapTileData, false);
        System.out.println("Wrote");
    }

}
