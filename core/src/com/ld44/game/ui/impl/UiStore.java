package com.ld44.game.ui.impl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ld44.game.assets.Assets;
import com.ld44.game.state.StateManager;
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.Skins;
import com.ld44.game.ui.UiContainer;

public class UiStore extends UiContainer {

    private Hud hud;

    public UiStore(Hud hud) {
        super(Skins.Holo_Dark_Hdpi.SKIN, null);
        this.hud = hud;
    }

    @Override
    public void create() {
        this.getRootTable().setPosition(this.getRootTable().getX() + 240, this.getRootTable().getY());
        this.getRootTable().addActor(new StoreButtonDoubleSmall());
    }


}

abstract class StoreButton extends ImageButton {

    private int price;

    public StoreButton(int price, String imageUp, String imageDown, String imageHover) {
        super(new SpriteDrawable(Assets.getInstance().getSprite(imageUp)), new SpriteDrawable(Assets.getInstance().getSprite(imageDown)));
        this.getStyle().imageOver = new SpriteDrawable(Assets.getInstance().getSprite(imageHover));

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                paid();
            }
        });

        this.price = price;

        TextTooltip tooltip = new TextTooltip(this.getName() + "\n(" + this.getDescription() + ")\n- Cost: " + this.getPrice(), Skins.Arcade.SKIN);
        tooltip.setInstant(true);
        this.addListener(tooltip);
    }

    public abstract void paid();

    public abstract String getName();

    public abstract String getDescription();

    public int getPrice() {
        return price;
    }

}

class StoreButtonDoubleSmall extends StoreButton {

    public StoreButtonDoubleSmall() {
        super(2,"ui/ship_double_small.png", "ui/ship_double_small_down.png", "ui/ship_double_small_hover.png");
    }

    @Override
    public void paid() {

    }

    @Override
    public String getName() {
        return ("Double Cannon Frigate");
    }

    @Override
    public String getDescription() {
        return ("Twice the firepower!");
    }

}