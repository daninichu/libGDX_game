package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.gameObjects.IGameObject;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    public static final float VIEW_WIDTH = 24*MyGame.TILE_SIZE;
    public static final float VIEW_HEIGHT = 18*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));

    private Map map;
    private ViewableEntity player;
    private Array<? extends ViewableEntity> entities;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(MyGame game, ViewableEntity player) {
        super(game);
        this.player = player;
        font.setUseIntegerPositions(false);
        font.getData().setScale(VIEW_HEIGHT/400);
    }

    @Override
    public void show() {
        super.show();

        this.map = game.getMap();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), batch);
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        reset();
    }

    public void reset(){
        mapRenderer.setMap(map.getTiledMap());
        camera.position.set(player.getCenterPos(), 0);
    }

    @Override
    public void render(float deltaTime) {
        entities = map.getEntities();
        viewport.apply();
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
                draw();
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
                draw();
            }
            case Dialogue -> {
                followPlayerWithCamera(deltaTime);
                draw();
                game.ui.renderDialogue();
            }
            case Inventory -> {
                game.setLoadState(MyGame.LoadState.LoadStart);
                game.setScreen(InventoryScreen.class);
            }
        }
    }

    private void draw(){
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 0);
//        ScreenUtils.clear(Color.CLEAR);
        mapRenderer.setView(camera);
        mapRenderer.render();
        entities.sort(comparator);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getTiledMap().getLayers().get("Ground"));
        for(ViewableEntity e : entities){
            if(e.getTexture() != null){
                Vector2 p = e.drawPos();
                batch.draw(e.getTexture(), p.x, p.y);
            }
            if(e.getHealth() > 0){
                font.draw(batch, e.getHealth()+" HP", e.getCenterX()-10, e.getCenterY() + 50);
            }
        }
        for(IGameObject object : map.getObjects()){
            if(object.canInteract()){
                font.draw(batch, "E", object.getCenterX(), object.getCenterY() + 40);
            }
        }
        batch.end();
        renderDebug();
    }

    private void followPlayerWithCamera(float deltaTime){
        camera.position.lerp(new Vector3(player.getCenterPos(), 0), 5*deltaTime);
        viewport.apply();
    }

    private void renderDebug(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Rectangle r : map.getCollisionBoxes()){
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        for(ViewableEntity e : entities){
            Rectangle r = e.locateHurtbox();
            if(r != null){
//                shapeRenderer.rect(r.x, r.y, r.width, r.height);
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
        viewport.update(width, height);
        game.ui.resize(width, height);
    }

    @Override
    public void dispose(){
        super.dispose();
        game.ui.dispose();
    }
}
