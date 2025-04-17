package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class GameOverScreen extends AbstractScreen{
    private Player player;
    private Stage stage;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    private Label text;

    public GameOverScreen(MyGame game, Player player) {
        super(game);
        this.player = player;
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        this.viewport = new ExtendViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        stage = new Stage(viewport, batch);
        font.setUseIntegerPositions(false);
        font.getData().setScale(2f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        text = new Label("Game Over", labelStyle);
        stage.addActor(text);
    }

    @Override
    public void render(float deltaTime){
        ScreenUtils.clear(Color.BLACK);

        if(player.dead())
            gameOverText(deltaTime);
        else
            deathAnimation(deltaTime);
    }

    private void deathAnimation(float deltaTime){
        viewport.getCamera().position.set(player.getCenterPos(), 0);
        viewport.apply();

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        Vector2 drawPos = player.drawPos();
        batch.draw(player.getTexture(), drawPos.x, drawPos.y);
        batch.end();

        fadeTime += deltaTime;
        if(fadeTime > fadeDuration)
            player.update(deltaTime);
    }

    private void gameOverText(float deltaTime){
        unfadeFromBlack(deltaTime);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        text.setAlignment(Align.center);
        text.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        text.setSize(1,1);
    }

    @Override
    public void dispose(){
        stage.dispose();
        font.dispose();
    }
}
