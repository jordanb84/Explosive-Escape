package com.ld44.game.state.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld44.game.assets.Assets;
import com.ld44.game.entity.Entity;
import com.ld44.game.map.Map;
import com.ld44.game.map.MapDefinition;
import com.ld44.game.map.MapLayer;
import com.ld44.game.map.io.MapExporter;
import com.ld44.game.map.io.MapImporter;
import com.ld44.game.state.State;
import com.ld44.game.state.StateManager;
import com.ld44.game.tile.Tile;
import com.ld44.game.tile.TileType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StateEditor extends State {

    private Map map;

    private MapDefinition mapDefinition;

    private int selectedLayer = 0;

    private TileType selectedTile = TileType.Stone;

    private boolean choosingTile;

    private boolean completedTileSelection;

    private Sprite editorBackground;

    private Rectangle mouseBody = new Rectangle();

    private final int SCROLL_SPEED = 50;

    private OrthographicCamera tileChooserCamera;

    public StateEditor(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {
        MapDefinition mapDefinition = new MapDefinition(40, 30, 16, 16, 3);

        List<MapLayer> tileLayers = new ArrayList<MapLayer>();
        tileLayers.add(new MapLayer(TileType.Grass, mapDefinition));
        tileLayers.add(new MapLayer(TileType.Air, mapDefinition));
        tileLayers.add(new MapLayer(TileType.Air, mapDefinition));
        tileLayers.add(new MapLayer(TileType.Air, mapDefinition));

        List<Entity> entities = new ArrayList<Entity>();

        this.map = new Map(tileLayers, mapDefinition, entities);
        this.mapDefinition = mapDefinition;

        this.editorBackground = Assets.getInstance().getSprite("editorBackground.png");
        this.editorBackground.setAlpha(0.5f);

        this.tileChooserCamera = new OrthographicCamera();
        this.tileChooserCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.map.render(batch, camera);

        if(this.choosingTile) {
            this.editorBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.editorBackground.draw(batch);
            this.renderTileChooser(batch, camera);
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.pollScrollInput(camera);

        this.map.update(camera);

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        this.mouseBody.set(mousePosition.x, mousePosition.y, 0, 0);

        if(!this.choosingTile) {
            this.checkMouseTileOverlap();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            JFileChooser fileChooser = new JFileChooser();

            int selectedOption = fileChooser.showOpenDialog(null);

            if(selectedOption == JFileChooser.APPROVE_OPTION) {
                String path = "map/" + fileChooser.getSelectedFile().getName();
                MapExporter.exportMapToFile(this.map, path);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            JFileChooser fileChooser = new JFileChooser();

            int selectedOption = fileChooser.showOpenDialog(null);

            if(selectedOption == JFileChooser.APPROVE_OPTION) {
                String path = "map/" + fileChooser.getSelectedFile().getName();
                this.map = MapImporter.importMapFromFile(path, this.mapDefinition, true);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            this.toggleTileChooser();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            this.selectedLayer++;

            if(this.selectedLayer >= this.map.getTileLayers().size()) {
                this.selectedLayer = this.map.getTileLayers().size() - 1;
            }

            Gdx.graphics.setTitle("Layer: " + this.selectedLayer);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            this.selectedLayer--;

            if(this.selectedLayer < 0) {
                this.selectedLayer = 0;
            }

            Gdx.graphics.setTitle("Layer: " + this.selectedLayer);
        }

    }

    public void renderTileChooser(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(this.tileChooserCamera.combined);

        Vector3 tileChooserMouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.tileChooserCamera.unproject(tileChooserMouse);

        Rectangle tileChooserMouseBody = new Rectangle(tileChooserMouse.x, tileChooserMouse.y, 0, 0);

        int rowIndex = 0;
        int tileIndex = 0;

        for(TileType tile : TileType.values()) {
            Rectangle tileBody = new Rectangle(tileIndex * tile.SPRITE.getWidth(), rowIndex * tile.SPRITE.getHeight(), tile.SPRITE.getWidth(), tile.SPRITE.getHeight());

            tile.SPRITE.setPosition(tileBody.getX(), tileBody.getY());
            tile.SPRITE.draw(batch);

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (tileChooserMouseBody.overlaps(tileBody)) {
                    this.selectedTile = tile;
                    this.completedTileSelection = true;
                }
            }

            if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && this.completedTileSelection) {
                this.toggleTileChooser();
            }

            tileIndex++;

            if(tileIndex > 2) {
                tileIndex = 0;
                rowIndex++;
            }
        }

        batch.setProjectionMatrix(camera.combined);
    }

    private void checkMouseTileOverlap() {
        int tileIndex = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int rowTile = 0; rowTile < this.mapDefinition.getMapWidth(); rowTile++) {
                Vector2 position = new Vector2(rowTile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                Tile tile = this.getSelectedLayerTiles().get(tileIndex).TILE;

                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    if(tile.getBody(position).overlaps(this.mouseBody)) {
                        this.map.getTileLayers().get(this.selectedLayer).setTile(tileIndex, this.selectedTile);
                        this.getSelectedLayerTiles().set(tileIndex, this.selectedTile);
                    }
                }

                tileIndex++;
            }
        }
    }

    public void pollScrollInput(OrthographicCamera camera) {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.add(0, this.SCROLL_SPEED * Gdx.graphics.getDeltaTime(), 0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.add(0, -this.SCROLL_SPEED * Gdx.graphics.getDeltaTime(), 0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.add(-this.SCROLL_SPEED * Gdx.graphics.getDeltaTime(), 0, 0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.add(this.SCROLL_SPEED * Gdx.graphics.getDeltaTime(), 0, 0);
        }

        camera.update();
    }

    public List<TileType> getSelectedLayerTiles() {
        return this.map.getTileLayers().get(this.selectedLayer).getTiles();
    }

    public void toggleTileChooser() {
        this.choosingTile = !this.choosingTile;

        this.completedTileSelection = false;
    }

    @Override
    public void resize(int width, int height) {

    }

}
