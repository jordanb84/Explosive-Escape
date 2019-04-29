package com.ld44.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.impl.EntityPlayer;
import com.ld44.game.map.Map;
import com.ld44.game.ship.impl.DoubleCannonDestroyerShip;
import com.ld44.game.ship.impl.DoubleCannonFrigateShip;
import com.ld44.game.state.StateManager;
import com.ld44.game.ui.Hud;
import com.ld44.game.ui.Skins;
import com.ld44.game.ui.UiContainer;

import java.util.HashMap;

public class UiStore extends UiContainer {

    private HashMap<Unlocks, StoreButton> unlocks;

    public UiStore(Hud hud) {
        super(hud, Skins.Holo_Dark_Hdpi.SKIN, null, true);
    }

    @Override
    public void create() {
        this.getRootTable().setPosition(this.getRootTable().getX() + 240, this.getRootTable().getY());

        StoreButtonDoubleSmall doubleSmall = new StoreButtonDoubleSmall(this, this.getHud(), this.getHud().getPlayer());
        this.getRootTable().addActor(doubleSmall);

        StoreButtonDoubleMedium doubleMedium = new StoreButtonDoubleMedium(this, this.getHud(), this.getHud().getPlayer());
        doubleMedium.setPosition(doubleMedium.getX() + 80, doubleMedium.getY() + 20);
        this.getRootTable().addActor(doubleMedium);

        StoreButtonBoss boss = new StoreButtonBoss(this, this.getHud(), this.getHud().getPlayer());
        boss.setPosition(doubleMedium.getX() + 200, boss.getY() + boss.getHeight() / 2);
        this.getRootTable().addActor(boss);

        this.unlocks = new HashMap<Unlocks, StoreButton>();

        System.out.println(this.unlocks + "/" + doubleSmall);
        this.unlocks.put(Unlocks.FRIGATE, doubleSmall);
        this.unlocks.put(Unlocks.DESTROYER, doubleMedium);
        this.unlocks.put(Unlocks.BOSS, boss);

        //this.getStage().setDebugAll(true);
    }

    public void unlock(Unlocks unlock) {
        this.unlocks.get(unlock).unlock();
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        Gdx.input.setInputProcessor(this.getStage());

        //System.out.println("Store size is at " + this.getStage().getViewport().getWorldWidth() + "/" + this.getStage().getViewport().getWorldHeight());
    }

    @Override
    public void resize (int width, int height) {
        super.resize(width, height);
        //System.out.println("Resized store");
        //this.rootStage.getViewport().setScreenSize(width, height);
    }

    public void resetLocks() {
        for(StoreButton storeButton : this.unlocks.values()) {
            storeButton.resetLock();
        }
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

    private SpriteDrawable lockedUp;
    private SpriteDrawable lockedDown;
    private SpriteDrawable lockedHover;

    private UiStore store;

    private TextTooltip tooltip;

    private String noLock;

    private Map map;

    public StoreButton(UiStore store, final Hud hud, EntityPlayer player, final int price, String imageUp, String imageDown, String imageHover, String imageUpLocked, String imageDownLocked, String imageHoverLocked) {
        super(new SpriteDrawable(Assets.getInstance().getSprite(imageUpLocked)), new SpriteDrawable(Assets.getInstance().getSprite(imageDownLocked)));
        this.getStyle().imageOver = new SpriteDrawable(Assets.getInstance().getSprite(imageHoverLocked));

        this.locked = true;

        this.map = player.getMap();

        this.unlockedUp = new SpriteDrawable(Assets.getInstance().getSprite(imageUp));
        this.unlockedDown = new SpriteDrawable(Assets.getInstance().getSprite(imageDown));
        this.unlockedHover = new SpriteDrawable(Assets.getInstance().getSprite(imageHover));

        this.lockedUp = new SpriteDrawable(Assets.getInstance().getSprite(imageUpLocked));
        this.lockedDown = new SpriteDrawable(Assets.getInstance().getSprite(imageDownLocked));
        this.lockedHover = new SpriteDrawable(Assets.getInstance().getSprite(imageHoverLocked));

        this.price = price;

        this.noLock = this.getName() + "\n(" + this.getDescription() + ")\n- Cost: " + this.getPrice();

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println(locked);
                if(hud.getCash() >= price) {
                    if(!locked) {
                        hud.modifyCash(-price);
                        tooltip.getActor().setText(noLock);
                        paid();
                    }
                }
            }
        });

        this.tooltip = new TextTooltip(this.getLockedText() + "\n" + noLock, Skins.Arcade.SKIN);
        this.tooltip.setInstant(true);
        this.addListener(this.tooltip);

        this.player = player;

        this.store = store;
    }

    public abstract void paid();

    public abstract String getName();

    public abstract String getDescription();

    public int getPrice() {
        return price;
    }

    public EntityPlayer getPlayer() {
        return this.map.getPlayer();
    }

    public void unlock() {
        if(this.locked) {
            this.locked = false;

            this.getStyle().imageUp = this.unlockedUp;
            this.getStyle().imageDown = this.unlockedDown;
            this.getStyle().imageOver = this.unlockedHover;

            this.tooltip.getActor().setText(this.noLock);
        }
    }

    public UiStore getStore() {
        return this.store;
    }

    public abstract String getLockedText();

    public abstract void resetLock();

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void lock() {
        this.tooltip.getActor().setText(this.getLockedText() + "\n" + this.noLock);
        this.locked = true;

        this.getStyle().imageUp = this.lockedUp;
        this.getStyle().imageDown = this.lockedDown;
        this.getStyle().imageOver = this.lockedHover;
    }

}

class StoreButtonDoubleSmall extends StoreButton {

    private boolean bought;

    public StoreButtonDoubleSmall(UiStore store, Hud hud, EntityPlayer player) {
        super(store, hud, player,2,"ui/ship_double_small.png", "ui/ship_double_small_down.png", "ui/ship_double_small_hover.png", "ui/ship_double_small.png", "ui/ship_double_small_down.png", "ui/ship_double_small_hover.png");
        this.unlock();
    }

    @Override
    public void paid() {
        System.out.println("Bought");
        this.getPlayer().setPlayerShip(new DoubleCannonFrigateShip(this.getPlayer().getMap(), this.getPlayer()));
        this.getStore().unlock(Unlocks.DESTROYER);

        if(!this.bought) {
            this.getPlayer().getMap().spawnEnemies(true);
            this.bought = true;
        }
    }

    @Override
    public String getName() {
        return ("Double Cannon Frigate");
    }

    @Override
    public String getDescription() {
        return ("Twice the firepower, more speed!");
    }

    @Override
    public String getLockedText() {
        return ("");
    }

    @Override
    public void resetLock() {
        this.setLocked(false);
    }

}

class StoreButtonBoss extends StoreButton {

    public StoreButtonBoss(UiStore store, Hud hud, EntityPlayer player) {
        super(store, hud, player, 2, "ui/boss.png", "ui/boss_down.png", "ui/boss_hover.png", "ui/boss_locked.png", "ui/boss_down_locked.png", "ui/boss_hover_locked.png");
        //this.unlock();
    }

    @Override
    public void paid() {
        this.getStore().getHud().getPlayer().getMap().startBossBattle();
        //this.getPlayer().setPlayerShip(new DoubleCannonDestroyerShip(this.getPlayer().getMap(), this.getPlayer()));
    }

    @Override
    public String getName() {
        return ("Find and Challenge the Loot Flagship");
    }

    @Override
    public String getDescription() {
        return ("Gain your freedom!");
    }

    @Override
    public String getLockedText() {
        return ("[Locked - Requires Double Cannon Destroyer]");
    }

    @Override
    public void resetLock() {
        this.lock();
    }

}


class StoreButtonDoubleMedium extends StoreButton {

    public StoreButtonDoubleMedium(UiStore store, Hud hud, EntityPlayer player) {
        super(store, hud, player,0,"ui/medium_double_side.png", "ui/medium_double_side_down.png", "ui/medium_double_side_hover.png", "ui/medium_double_side_locked.png", "ui/medium_double_side_down_locked.png", "ui/medium_double_side_hover_locked.png");
    }

    @Override
    public void paid() {
        this.getPlayer().setPlayerShip(new DoubleCannonDestroyerShip(this.getPlayer().getMap(), this.getPlayer()));
        this.getStore().unlock(Unlocks.BOSS);
    }

    @Override
    public String getName() {
        return ("Double Cannon Destroyer");
    }

    @Override
    public String getDescription() {
        return ("More guns, durability and speed!");
    }

    @Override
    public String getLockedText() {
        return ("[Locked - Requires Double Cannon Frigate]");
    }

    @Override
    public void resetLock() {
        this.lock();
    }

}