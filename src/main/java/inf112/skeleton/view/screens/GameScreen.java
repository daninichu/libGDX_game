package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    private static final float VIEW_WIDTH = 24*MyGame.TILE_SIZE;
    private static final float VIEW_HEIGHT = 18*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();

    private Map map;
    private ViewableEntity player;
    private Array<ViewableEntity> entities;

    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private UI ui;

    public GameScreen(MyGame game) {
        super(game);
        this.map = game.getMap();
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
        ui = new UI(entities);
    }

    Texture t = new Texture(Gdx.files.internal("sprite.png"));
    @Override
    public void render(float deltaTime) {
        long time = System.nanoTime();
        ScreenUtils.clear(Color.BLACK);

        map.update(deltaTime);
        followPlayerWithCamera(deltaTime);

        mapRenderer.setView(camera);
        mapRenderer.render();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        entities.sort(comparator);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(ViewableEntity e : entities){
            if(e instanceof Player)
                shapeRenderer.setColor(Color.WHITE);
            else if(e instanceof Enemy){
                shapeRenderer.setColor(Color.RED);
            }
            shapeRenderer.rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
//            batch.draw(t, e.getX(), e.getY());
        }
        batch.end();

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(ViewableEntity e : entities){
            if(e instanceof Enemy enemy){
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(e.getCenterX(), enemy.getCenterY(), Enemy.vision);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(e.getCenterX(), enemy.getCenterY(), enemy.getAttackRange());
            }
        }
        shapeRenderer.end();
        ui.debug(deltaTime);

//        Gdx.app.log("Render time", (System.nanoTime()-time)/1000000f+" ms");
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
