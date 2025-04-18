package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
import java.text.DecimalFormat;

public class GameScreen extends AbstractScreen{
    public static final float VIEW_WIDTH = 20*MyGame.TILE_SIZE;
    public static final float VIEW_HEIGHT = 15*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();

    private float mapWidth;
    private float mapHeight;
    private Map map;
    private ViewableEntity player;
    private Array<? extends ViewableEntity> entities;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Stage stage;
    private Label.LabelStyle labelStyle;
    private Label dialogue;

    public GameScreen(MyGame game, ViewableEntity player) {
        super(game);
        this.player = player;
    }

    @Override
    public void show() {
        super.show();
        font.getData().setScale(0.3f);

        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        uiViewport = new ExtendViewport(160, 120);

        stage = new Stage(uiViewport);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        dialogue = new Label("", labelStyle);
        stage.addActor(dialogue);

        map = game.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), gameBatch);
        reset();
    }

    public void reset(){
        mapRenderer.setMap(map.getTiledMap());
        MapProperties mapProps = map.getTiledMap().getProperties();
        mapWidth = mapProps.get("width", int.class) * mapProps.get("tilewidth", int.class);
        mapHeight = mapProps.get("height", int.class) * mapProps.get("tileheight", int.class);

        camera.position.set(calculateCameraPos());
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
                camera.position.lerp(new Vector3(player.getCenterPos(), 0), 8*deltaTime);
                gameBatch.setProjectionMatrix(camera.combined);
                gameBatch.begin();
                draw(gameBatch, player.getTexture(), player.drawPos());
                gameBatch.end();
                if(resetFadeTimer()){
                    game.setScreen(GameOverScreen.class);
                }
            }
        }
        if(player.getHealth() <= 0)
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
        renderDebug();
    }

    private void renderUi(){
        float barX = 10;
        float barY = stage.getHeight() - 10;
        float barWidth = player.getMaxHealth();
        float barHeight = 4;
        float healthPercentage = (float) player.getHealth() / player.getMaxHealth();
        float healthBarWidth = barWidth * healthPercentage;

        shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.8f, 0.3f, 0.3f, 1);
        shapeRenderer.rect(barX, barY, healthBarWidth, barHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.9f, 0.8f, 0.8f, 1);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();

        uiBatch.setProjectionMatrix(uiViewport.getCamera().combined);
        uiBatch.begin();
        font.draw(uiBatch, player.getHealth()+"/"+ player.getMaxHealth()+"HP", barX + barWidth + 4, barY);

        for(IGameObject object : map.getGameObjects())
            if(object.canInteract()){
                font.draw(uiBatch, "E: Interact", barX, barY - 10);
                break;
            }
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

    private Vector3 calculateCameraPos(){
        float x = Math.min(Math.max(player.getCenterX(), gameViewport.getWorldWidth()/2), mapWidth - gameViewport.getWorldWidth()/2);
        float y = Math.min(Math.max(player.getCenterY(), gameViewport.getWorldHeight()/2), mapHeight - gameViewport.getWorldHeight()/2);
        if(mapWidth < gameViewport.getWorldWidth())
            x = mapWidth / 2;
        if(mapHeight < gameViewport.getWorldHeight())
            y = mapHeight / 2;
        return new Vector3(x, y, 0);
    }

    private void followPlayerWithCamera(float deltaTime){
        camera.position.lerp(calculateCameraPos(), 5*deltaTime);
        gameViewport.apply();
    }

    private void renderDebug(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for(ViewableEntity e : entities){
            for(Point cell : HashGrid.getOccupiedCells(e.locateHurtbox())){
                Rectangle r = Box.cell(cell);
//                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }
        }
        shapeRenderer.setColor(Color.WHITE);
//        shapeRenderer.circle(player.getCenterPos().x, player.getCenterPos().y, Enemy.vision);
        for(Rectangle r : map.getCollisionBoxes()){
//            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        for(ViewableEntity e : entities){
            Rectangle r = e.locateHurtbox();
            if(r != null){
//                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }
            if(e instanceof Enemy enemy){
                Line l = enemy.getRay();
                if(l != null){
//                    shapeRenderer.rectLine(l.x1, l.y1, l.x2, l.y2, 1);
                }
            }
        }
        shapeRenderer.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(1,0,0,0.3f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Circle hitbox : map.getHitboxes()){
//            shapeRenderer.circle(hitbox.x, hitbox.y, hitbox.radius);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height){
        gameViewport.update(width, height);
        uiViewport.update(width, height, true);
        centerDialogueLabel();
    }

    @Override
    public void dispose(){
        super.dispose();
        stage.dispose();
    }
}
