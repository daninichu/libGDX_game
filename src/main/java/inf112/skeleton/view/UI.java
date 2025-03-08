package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.MyGame;

import java.text.DecimalFormat;

public class UI {
    private static final int VIEW_WIDTH = 40;
    private static final int VIEW_HEIGHT = 30;
    private static DecimalFormat df = new DecimalFormat("#.#");

    private ViewableEntity player;
    private Stage stage;

    private Viewport viewport;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    private Label.LabelStyle labelStyle;
    private Label dialogue;

    public UI(ViewableEntity player) {
        this.player = player;
        this.viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT);
        this.stage = new Stage(viewport);

        font.setUseIntegerPositions(false);
        font.getData().setScale((float) VIEW_HEIGHT / MyGame.SCREEN_HEIGHT*2);
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        dialogue = new Label("", labelStyle);
        stage.addActor(dialogue);
    }

    private void centerDialogueLabel() {
        float x = (stage.getWidth() - dialogue.getWidth()) / 2;
        float y = 10;
        dialogue.setPosition(x, y);
    }

    public void setDialogue(String newDialogue) {
        stage.clear();
        dialogue = new Label(newDialogue, labelStyle);
        centerDialogueLabel();
        stage.addActor(dialogue);
    }

    public void renderDialogue() {
        viewport.apply();
        stage.act();
        stage.draw();
    }

    public void debug(float deltaTime){

//        batch.setProjectionMatrix(stage.getCamera().combined);
//        batch.begin();
//        String pos = "("+df.format(player.getX())+", "+df.format(player.getY())+")";
//        font.draw(batch, pos, 2, viewport.getWorldHeight()-2);
//        batch.end();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
        centerDialogueLabel();
    }

    public void dispose () {
        stage.dispose();
    }
}
