package com.ld44.game.tile;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ld44.game.assets.Assets;

public enum  TileType {
    Air(), Grass(), Stone(), Water("tile/water5.png")
    ;

    TileType() {
        this.TILE = new Tile(this);
        this.SPRITE = Assets.getInstance().getSprite("tile/" + this.name().toLowerCase() + ".png");
    }

    TileType(Tile tile) {
        this.TILE = tile;
        this.SPRITE = Assets.getInstance().getSprite("tile/" + this.name().toLowerCase() + ".png");
    }

    TileType(String sprite) {
        this.TILE = new Tile(this);
        this.SPRITE = Assets.getInstance().getSprite(sprite);
    }

    TileType(Tile tile, String sprite){
        this.TILE = tile;
        this.SPRITE = Assets.getInstance().getSprite(sprite);
    }

    public final Tile TILE;
    public final Sprite SPRITE;

}
