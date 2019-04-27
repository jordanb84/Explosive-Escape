package com.ld44.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ld44.game.assets.Assets;

public enum Skins {
    Holo_Dark_Hdpi("skin/holo/Holo-dark-hdpi.json"), Arcade("skin/arcade/arcade-ui.json")
    ;

    Skins(String path) {
        this.SKIN = Assets.getInstance().getSkin(path);
    }

    public final Skin SKIN;

}