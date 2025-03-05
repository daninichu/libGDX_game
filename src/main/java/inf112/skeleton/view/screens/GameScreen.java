package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.MyInputProcessor;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.objects.Sign;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.TextBox;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    public enum State {
        Play, Dialogue
    }
    private static final float VIEW_WIDTH = 24*MyGame.TILE_SIZE;
    private static final float VIEW_HEIGHT = 18*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();

    private State state = State.Play;
    private Map map;
    private ViewableEntity player;
    private Array<ViewableEntity> entities;

    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private UI ui;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    private Stage stage;

    public GameScreen(MyGame game) {
        super(game);
        this.map = game.getMap();
        this.entities = map.getEntities();
        this.player = entities.get(0);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void show() {
        super.show();
        camera = new OrthographicCamera();
        camera.position.set(player.getCenterPos(), 0);
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap());
    }

    @Override
    public void render(float deltaTime) {
        long time = System.nanoTime();
        ScreenUtils.clear(Color.BLACK);

        if(state == State.Play) {
            map.update(deltaTime);
        }

        followPlayerWithCamera(deltaTime);
        mapRenderer.setView(camera);
        mapRenderer.render();
        shapeRenderer.setProjectionMatrix(camera.combined);

        entities.sort(comparator);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(ViewableEntity e : entities){
            if(e.getTexture() != null){
                batch.draw(e.getTexture(), e.getX(), e.getY());
            }
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Rectangle r : map.getCollisionBoxes()){
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        shapeRenderer.end();

        if(state == State.Dialogue){
            game.ui.renderDialogue();
        }
//        ui.debug(deltaTime);

//        Gdx.app.log("Render time", (System.nanoTime()-time)/1000000f+" ms");
    }

    private void followPlayerWithCamera(float deltaTime){
        camera.position.lerp(new Vector3(player.getCenterPos(), 0), 5*deltaTime);
        viewport.apply();
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
