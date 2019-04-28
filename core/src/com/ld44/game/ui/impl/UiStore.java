package com.ld44.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.ship.impl.DoubleCannonDestroyerShip;
import com.ld44.game.ship.impl.DoubleCannonFrigateShip;
import com.ld44.game.state.StateManager;
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.Skins;
import com.ld44.game.ui.UiContainer;

public class UiStore extends UiContainer {

    public UiStore(Hud hud) {
        super(hud, Skins.Holo_Dark_Hdpi.SKIN, null);
    }

    @Override
    public void create() {
        this.getRootTable().setPosition(this.getRootTable().getX() + 240, this.getRootTable().getY());
        this.getRootTable().addActor(new StoreButtonDoubleSmall(this.getHud(), this.getHud().getPlayer()));

        StoreButtonDoubleMedium doubleMedium = new StoreButtonDoubleMedium(this.getHud(), this.getHud().getPlayer());
        doubleMedium.setPosition(doubleMedium.getX() + 80, doubleMedium.getY() + 20);
        this.getRootTable().addActor(doubleMedium);
    }


}

abstract class StoreButton extends ImageButton {

    private int price;

    private EntityPlayer player;

    private Hud hud;

    private boolean locked;

    private SpriteDrawable unlockedUp;
    private SpriteDrawable unlockedDown;
    private SpriteDrawable unlockedHover;

    public StoreButton(final Hud hud, EntityPlayer player, final int price, String imageUp, String imageDown, String imageHover, String imageUpLocked, String imageDownLocked, String imageHoverLocked) {
        super(new SpriteDrawable(Assets.getInstance().getSprite(imageUpLocked)), new SpriteDrawable(Assets.getInstance().getSprite(imageDownLocked)));
        this.getStyle().imageOver = new SpriteDrawable(Assets.getInstance().getSprite(imageHoverLocked));

        this.unlockedUp = new SpriteDrawable(Assets.getInstance().getSprite(imageUp));
        this.unlockedDown = new SpriteDrawable(Assets.getInstance().getSprite(imageDown));
        this.unlockedHover = new SpriteDrawable(Assets.getInstance().getSprite(imageHover));

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(hud.getCash() >= price) {
                    if(!locked) {
                        hud.modifyCash(-price);
                        paid();
                    }
                }
            }
        });

        this.price = price;

        TextTooltip tooltip = new TextTooltip(this.getName() + "\n(" + this.getDescription() + ")\n- Cost: " + this.getPrice(), Skins.Arcade.SKIN);
        tooltip.setInstant(true);
        this.addListener(tooltip);

        this.player = player;
    }

    public abstract void paid();

    public abstract String getName();

    public abstract String getDescription();

    public int getPrice() {
        return price;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void unlock() {
        this.locked = false;

        this.getStyle().imageUp = this.unlockedUp;
        this.getStyle().imageDown = this.unlockedDown;
        this.getStyle().imageOver = this.unlockedHover;
    }

}

class StoreButtonDoubleSmall extends StoreButton {

    public StoreButtonDoubleSmall(Hud hud, EntityPlayer player) {
        super(hud, player,30,"ui/ship_double_small.png", "ui/ship_double_small_down.png", "ui/ship_double_small_hover.png", "ui/ship_double_small.png", "ui/ship_double_small_down.png", "ui/ship_double_small_hover.png");
    }

    @Override
    public void paid() {
        this.getPlayer().setPlayerShip(new DoubleCannonFrigateShip(this.getPlayer().getMap(), this.getPlayer()));
    }

    @Override
    public String getName() {
        return ("Double Cannon Frigate");
    }

    @Override
    public String getDescription() {
        return ("Twice the firepower and more speed!");
    }

}


class StoreButtonDoubleMedium extends StoreButton {

    public StoreButtonDoubleMedium(Hud hud, EntityPlayer player) {
        super(hud, player,2,"ui/medium_double_side.png", "ui/medium_double_side_down.png", "ui/medium_double_side_hover.png", "ui/medium_double_side_locked.png", "ui/medium_double_side_down_locked.png", "ui/medium_double_side_hover_locked.png");
    }

    @Override
    public void paid() {
        this.getPlayer().setPlayerShip(new DoubleCannonDestroyerShip(this.getPlayer().getMap(), this.getPlayer()));
    }

    @Override
    public String getName() {
        return ("Double Cannon Destroyer");
    }

    @Override
    public String getDescription() {
        return ("Far more durable and packed with speed!");
    }

}