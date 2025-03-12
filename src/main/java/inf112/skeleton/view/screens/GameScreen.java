package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.view.DrawOrderComparator;
import inf112.skeleton.view.ViewableEntity;

public class GameScreen extends AbstractScreen{
    public static final float VIEW_WIDTH = 24*MyGame.TILE_SIZE;
    public static final float VIEW_HEIGHT = 18*MyGame.TILE_SIZE;
    private static final DrawOrderComparator comparator = new DrawOrderComparator();
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));

    private Map map;
    private ViewableEntity player;
    private Array<ViewableEntity> entities;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(MyGame game) {
        super(game);
        font.getData().setScale(VIEW_HEIGHT/400);
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        reset();
        viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    }

    public void reset(){
        map = game.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap());
        entities = map.getEntities();
        player = entities.get(0);
        camera.position.set(player.getCenterPos(), 0);
    }

    @Override
    public void render(float deltaTime) {
        long time = System.nanoTime();
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
            case LoadStart -> {
                fadeToBlack(deltaTime);
                if(resetFadeTimer()){
                    reset();
                    game.setState(MyGame.State.LoadEnd);
                }
            }
            case LoadEnd -> {
                followPlayerWithCamera(deltaTime);
                draw();
                unfadeFromBlack(deltaTime);
                if(resetFadeTimer())
                    game.setState(MyGame.State.Play);
            }
        }
//        Gdx.app.log("Render time", (System.nanoTime()-time)/1000000f+" ms");
    }

    private void draw(){
        ScreenUtils.clear(Color.CLEAR);
        mapRenderer.setView(camera);
        mapRenderer.render();

        entities.sort(comparator);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(ViewableEntity e : entities){
            if(e.getTexture() != null)
                batch.draw(e.getTexture(), e.getX(), e.getY());
        }
        for(GameObject object : map.getObjects()){
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
        for(Rectangle r : map.getCollisionBoxes())
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        for(ViewableEntity e : entities){
            Rectangle r = e.locateHurtbox();
            if(r != null)
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        shapeRenderer.setColor(Color.RED);
        for(Rectangle hitbox : map.getHitboxes()){
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
        shapeRenderer.end();
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
