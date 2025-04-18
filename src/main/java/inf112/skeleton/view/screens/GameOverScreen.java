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
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class GameOverScreen extends AbstractScreen{
    private Player player;
    private Stage stage;
    private Label text;

    public GameOverScreen(MyGame game, Player player) {
        super(game);
        this.player = player;
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        gameViewport = new ExtendViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        uiViewport = new FitViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        stage = new Stage(uiViewport, uiBatch);
        font.getData().setScale(2f);

        text = new Label("Game Over", new Label.LabelStyle(font, Color.WHITE));
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

        gameBatch.setProjectionMatrix(gameViewport.getCamera().combined);
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
        uiViewport.apply();
        stage.act();
        stage.draw();
        unfadeFromBlack(deltaTime);
        if(fadeTime < fadeDuration)
            return;


    }

    @Override
    public void resize(int width, int height){
        gameViewport.update(width, height);
        uiViewport.update(width, height, true);
        text.setAlignment(Align.center);
        text.setPosition(stage.getWidth()/2, stage.getHeight()/2);
        text.setSize(1,1);
    }

    @Override
    public void dispose(){
        stage.dispose();
        font.dispose();
    }
}
