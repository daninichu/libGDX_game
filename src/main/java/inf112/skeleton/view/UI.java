package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.text.DecimalFormat;

public class UI {
    private ViewableEntity player;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private SpriteBatch batch;

    private static DecimalFormat df = new DecimalFormat("#.#");

    public UI(ViewableEntity player) {
        this.player = player;
//        font.setUseIntegerPositions(false);
//        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();
        viewport = new ExtendViewport(5,5);
        this.stage = new Stage(viewport, batch);

        //font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render (float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void debug(float deltaTime){
//        viewport.apply();
        stage.act(deltaTime);
        stage.draw();
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        String coordinates = "("+df.format(player.getX())+", "+df.format(player.getY())+")";
        font.draw(batch, coordinates, 0,  viewport.getWorldHeight() - 50);
        font.draw(batch, ""+viewport.getWorldWidth(), 5,  3);
        batch.end();
        System.out.println(viewport.getWorldHeight());
        System.out.println(Gdx.graphics.getHeight());
        System.out.println(viewport.getWorldHeight() / Gdx.graphics.getHeight());
    }

    public void dispose () {
        stage.dispose();
    }

}
