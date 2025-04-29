package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.gameobjects.IGameObject;
import inf112.skeleton.util.Box;
import inf112.skeleton.util.Line;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.ViewableEntity;

import java.awt.Point;

public class GameScreen extends AbstractScreen{
    public static final float VIEW_WIDTH = 20*MyGame.TILE_SIZE;
    public static final float VIEW_HEIGHT = 15*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();

    private float mapWidth, mapHeight;
    private Map map;
    private ViewableEntity player;
    private Array<? extends ViewableEntity> entities;

    private OrthographicCamera camera = new OrthographicCamera();
    private OrthogonalTiledMapRenderer mapRenderer;
    private Label dialogue;

    public GameScreen(MyGame game, ViewableEntity player) {
        super(game);
        this.player = player;
    }

    @Override
    public void show() {
        super.show();
        font.getData().setScale(0.3f);

        gameViewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT);
        uiViewport = new ExtendViewport(160, 120);

        stage = new Stage(uiViewport);
        dialogue = new Label("", labelStyle);
        stage.addActor(dialogue);

        map = game.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), gameBatch);
        reset();
    }

    private void reset(){
        mapRenderer.setMap(map.getTiledMap());
        MapProperties mapProps = map.getTiledMap().getProperties();
        mapWidth = mapProps.get("width", int.class) * mapProps.get("tilewidth", int.class);
        mapHeight = mapProps.get("height", int.class) * mapProps.get("tileheight", int.class);

        camera.position.set(calculateCameraPos(false));
        gameViewport.apply();
    }

    @Override
    public void render(float deltaTime) {
        entities = map.getEntities();
        gameViewport.apply();
        switch(game.getLoadState()){
            case LoadStart -> {
                fadeToBlack(deltaTime);
                if(resetFadeTimer()){
                    reset();
                    game.setLoadState(MyGame.LoadState.LoadEnd);
                }
                return;
            }
            case LoadEnd -> {
                renderGame();
                unfadeFromBlack(deltaTime);
                if(resetFadeTimer())
                    game.setLoadState(MyGame.LoadState.NotLoading);
                return;
            }
        }
        switch(game.getState()){
            case Play -> {
                map.update(deltaTime);
                followPlayerWithCamera(deltaTime);
                renderGame();
            }
            case Dialogue -> {
                followPlayerWithCamera(deltaTime);
                renderGame();
                renderDialogue();
            }
            case Inventory -> {
                game.setLoadState(MyGame.LoadState.LoadStart);
                game.setScreen(InventoryScreen.class);
            }
            case GameOver -> {
                renderGame();
                fadeToBlack(deltaTime/2);
                camera.position.lerp(calculateCameraPos(true), 8*deltaTime);
                gameBatch.setProjectionMatrix(camera.combined);
                gameBatch.begin();
                draw(gameBatch, player.getTexture(), player.drawPos());
                gameBatch.end();
                if(resetFadeTimer()){
                    game.setScreen(GameOverScreen.class);
                }
            }
        }
        if(player.getHp() <= 0)
            game.setState(MyGame.State.GameOver);
    }

    private void renderGame(){
        if(game.getLoadState() == MyGame.LoadState.LoadStart)
            return;
        ScreenUtils.clear(Color.CLEAR);
        mapRenderer.setView(camera);
        mapRenderer.render();

        entities.sort(comparator);
        gameBatch.setProjectionMatrix(camera.combined);
        gameBatch.begin();
        for(ViewableEntity e : entities){
            if(e.getTexture() != null)
                draw(gameBatch, e.getTexture(), e.drawPos());
        }
        if(mapRenderer.getMap().getLayers().get("Overlay") != null){
            mapRenderer.renderTileLayer((TiledMapTileLayer) mapRenderer.getMap().getLayers().get("Overlay"));
        }
        gameBatch.end();
        renderUi();
//        renderDebug();
    }

    private void renderUi(){
        Rectangle healthBar = new Rectangle(10, stage.getHeight() - 10, player.getHp(), 4);
        Rectangle bar = new Rectangle(healthBar).setWidth(player.getMaxHp());

        shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);
        draw(healthBar, new Color(0.8f, 0.3f, 0.3f, 1), ShapeRenderer.ShapeType.Filled);
        draw(bar, new Color(0.9f, 0.8f, 0.8f, 1), ShapeRenderer.ShapeType.Line);

        uiBatch.setProjectionMatrix(uiViewport.getCamera().combined);
        uiBatch.begin();
        font.draw(uiBatch, player.getHp()+"/"+ player.getMaxHp()+"HP", bar.x + bar.width + 4, bar.y);
        for(IGameObject object : map.getGameObjects())
            if(object.canInteract())
                font.draw(uiBatch, "E: Interact", bar.x, bar.y - 10);
        uiBatch.end();
    }

    private void centerDialogueLabel() {
        float x = (stage.getWidth() - dialogue.getWidth()) / 2;
        float y = 40;
        dialogue.setPosition(x, y);
    }

    public void setDialogue(String newDialogue) {
        stage.clear();
        dialogue = new Label(newDialogue, labelStyle);
        centerDialogueLabel();
        stage.addActor(dialogue);
    }

    private void renderDialogue() {
        uiViewport.apply();
        stage.act();
        stage.draw();
    }

    private Vector3 calculateCameraPos(boolean atPlayer){
        if(atPlayer)
            return new Vector3(player.getCenterPos(), 0);
        float x = Math.min(Math.max(player.getCenterX(), gameViewport.getWorldWidth()/2), mapWidth - gameViewport.getWorldWidth()/2);
        float y = Math.min(Math.max(player.getCenterY(), gameViewport.getWorldHeight()/2), mapHeight - gameViewport.getWorldHeight()/2);
        if(mapWidth < gameViewport.getWorldWidth())
            x = mapWidth / 2;
        if(mapHeight < gameViewport.getWorldHeight())
            y = mapHeight / 2;
        return new Vector3(x, y, 0);
    }

    private void followPlayerWithCamera(float deltaTime){
        camera.position.lerp(calculateCameraPos(false), 5*deltaTime);
        gameViewport.apply();
    }

    private void renderDebug(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(ViewableEntity e : entities)
            for(Point cell : HashGrid.getOccupiedCells(e.locateHurtbox()))
                draw(Box.cell(cell), Color.BLUE);
        for(Rectangle r : map.getCollisionBoxes())
            draw(r, Color.WHITE);
        for(ViewableEntity e : entities){
            draw(e.locateHurtbox(), Color.WHITE);
            if(e instanceof Enemy enemy)
                if(enemy.getRay() != null)
                    draw(enemy.getRay(), Color.WHITE);
        }
        shapeRenderer.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for(Circle hitbox : map.getHitboxes())
            draw(hitbox, new Color(1, 0, 0, 0.3f), ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        centerDialogueLabel();
    }
}
