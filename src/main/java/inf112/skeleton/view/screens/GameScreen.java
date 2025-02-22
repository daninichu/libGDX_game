package inf112.skeleton.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.model.Map;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    private static final float VIEW_WIDTH = 24*32;
    private static final float VIEW_HEIGHT = 18*32;

    private Map map;
    private ViewableEntity player;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private UI ui;

    public GameScreen(Game game){
        super(game);
        map = new Map("maps/grassMap.tmx");
        player = map.getPlayer();
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
        super.render(deltaTime);

        map.update(deltaTime);
        followPlayerWithCamera(deltaTime);

        mapRenderer.setView(camera);
        mapRenderer.render();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();
//        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        ui.debug(deltaTime);
        batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void followPlayerWithCamera(float deltaTime){
        float cameraDx = (player.getX() + player.getWidth()/2 - camera.position.x)*5*deltaTime;
        float cameraDy = (player.getY() + player.getHeight()/2 - camera.position.y)*5*deltaTime;
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
