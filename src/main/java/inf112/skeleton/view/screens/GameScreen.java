package inf112.skeleton.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    private static final float VIEW_WIDTH = 24*32;
    private static final float VIEW_HEIGHT = 18*32;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();

    private Map map;
    private ViewableEntity player;
    private Array<ViewableEntity> entities;

    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private UI ui;

    public GameScreen(Game game, Map map) {
        super(game);
        this.map = map;
        this.entities = map.getEntities();
        this.player = entities.get(0);
    }

    @Override
    public void show(){
        super.show();
        camera = new OrthographicCamera();
        camera.position.set(player.getCenterX(), player.getCenterY(), 0);
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap());
        ui = new UI(player);
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(Color.BLACK);

        map.update(deltaTime);
        followPlayerWithCamera(deltaTime);

        mapRenderer.setView(camera);
        mapRenderer.render();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        entities.sort(comparator);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(ViewableEntity e : entities){
            if(e instanceof Player)
                shapeRenderer.setColor(Color.WHITE);
            else if(e instanceof Enemy){
                shapeRenderer.setColor(Color.RED);
            }
            shapeRenderer.rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(ViewableEntity e : entities){
            if(e instanceof Enemy){
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(e.getCenterX(), e.getCenterY(), Enemy.vision);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(e.getCenterX(), e.getCenterY(), Enemy.attackRange);
            }
        }
        shapeRenderer.end();
        ui.debug(deltaTime);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void followPlayerWithCamera(float deltaTime){
        float cameraDx = (player.getCenterX() - camera.position.x)*5*deltaTime;
        float cameraDy = (player.getCenterY() - camera.position.y)*5*deltaTime;
        camera.position.add(cameraDx, cameraDy, 0);
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        ui.resize(width, height);
    }

    @Override
    public void dispose(){
        super.dispose();
        ui.dispose();
    }
}
