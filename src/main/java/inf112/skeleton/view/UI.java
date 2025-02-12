package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.GdxGame;

import java.text.DecimalFormat;

public class UI {
    private ViewableEntity player;
    private Stage stage;

    private static final float VIEWPORT_WIDTH = 40;
    private static final float VIEWPORT_HEIGHT = 30;

    private Viewport viewport = new ExtendViewport(VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));

    private SpriteBatch batch = new SpriteBatch();

    private static DecimalFormat df = new DecimalFormat("#.#");

    public UI(ViewableEntity player) {
        this.player = player;
        this.stage = new Stage(viewport, batch);

        font.setUseIntegerPositions(false);
        font.getData().setScale(VIEWPORT_HEIGHT / GdxGame.SCREEN_HEIGHT);
    }

    public void render (float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void debug(float deltaTime){
        viewport.apply();
//        stage.act(deltaTime);
//        stage.draw();
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        String pos = "("+df.format(player.getX())+", "+df.format(player.getY())+")";
        font.draw(batch, pos, 2, viewport.getWorldHeight()-2);
        font.draw(batch, "gdxHeight = "+Gdx.graphics.getHeight(), 2, 4);
        font.draw(batch, "gdxWidth = "+Gdx.graphics.getWidth(), 2, 6);
        font.draw(batch, "viewportWorldHeight = "+viewport.getWorldHeight(), 2, 8);
        font.draw(batch, "viewportWorldWidth = "+viewport.getWorldWidth(), 2, 10);
        font.draw(batch, "viewportScreenHeight = "+viewport.getScreenHeight(), 2, 12);
        font.draw(batch, "viewportScreenWidth = "+viewport.getScreenWidth(), 2, 14);
        font.draw(batch, "fontScaleY = "+font.getScaleY(), 2, 16);
        font.draw(batch, "fontScaleX = "+font.getScaleX(), 2, 18);

        batch.end();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
    }

}
