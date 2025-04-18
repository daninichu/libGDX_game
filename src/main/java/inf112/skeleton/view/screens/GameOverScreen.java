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
        this.gameViewport = new ExtendViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        stage = new Stage(gameViewport, gameBatch);
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
        gameViewport.getCamera().position.set(player.getCenterPos(), 0);
        gameViewport.apply();

        gameBatch.setProjectionMatrix(stage.getCamera().combined);
        gameBatch.begin();
        Vector2 drawPos = player.drawPos();
        gameBatch.draw(player.getTexture(), drawPos.x, drawPos.y);
        gameBatch.end();

        fadeTime += deltaTime;
        if(fadeTime > fadeDuration)
            player.update(deltaTime);
        if(player.dead())
            fadeTime = 0;
    }

    private void gameOverText(float deltaTime){
        stage.getViewport().apply();
        stage.act();
        stage.draw();
        unfadeFromBlack(deltaTime);
    }

    @Override
    public void resize(int width, int height){
        gameViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
        text.setAlignment(Align.center);
        text.setPosition(gameViewport.getWorldWidth()/2, gameViewport.getWorldHeight()/2);
        text.setSize(1,1);
    }

    @Override
    public void dispose(){
        stage.dispose();
        font.dispose();
    }
}
