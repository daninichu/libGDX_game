package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.enemies.Enemy;

import java.text.DecimalFormat;

public class UI {
    private ViewableEntity player;
    private Array<ViewableEntity> entities;
    private Stage stage;

    private static final int VIEW_WIDTH = 40;
    private static final int VIEW_HEIGHT = 30;

    private Viewport viewport = new ExtendViewport(VIEW_WIDTH, VIEW_HEIGHT);
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));

    private SpriteBatch batch = new SpriteBatch();

    private static DecimalFormat df = new DecimalFormat("#.#");

    public UI(Array<ViewableEntity> entities) {
        this.entities = entities;
        this.player = entities.get(0);
        this.stage = new Stage(viewport, batch);

        font.setUseIntegerPositions(false);
        font.getData().setScale((float) VIEW_HEIGHT / MyGame.SCREEN_HEIGHT);
    }

    public void render (float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void debug(float deltaTime){
        viewport.apply();

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        String pos = "("+df.format(player.getX())+", "+df.format(player.getY())+")";
        font.draw(batch, pos, 2, viewport.getWorldHeight()-2);
//        font.draw(batch, "gdxHeight = "+Gdx.graphics.getHeight(), 2, 4);
//        font.draw(batch, "gdxWidth = "+Gdx.graphics.getWidth(), 2, 6);
//        font.draw(batch, "viewportWorldHeight = "+viewport.getWorldHeight(), 2, 8);
//        font.draw(batch, "viewportWorldWidth = "+viewport.getWorldWidth(), 2, 10);
//        font.draw(batch, "viewportScreenHeight = "+viewport.getScreenHeight(), 2, 12);
//        font.draw(batch, "viewportScreenWidth = "+viewport.getScreenWidth(), 2, 14);
//        font.draw(batch, "fontScaleY = "+font.getScaleY(), 2, 16);
//        font.draw(batch, "fontScaleX = "+font.getScaleX(), 2, 18);
        for(ViewableEntity entity : entities){
            if(entity instanceof Enemy enemy2) {
//                font.draw(batch, enemy2.getState(), enemy2.getX(), entity.getY() + 1);
                font.draw(batch, enemy2.getState(), viewport.getWorldWidth()/2, 12);
            }
        }

        batch.end();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
    }

}
