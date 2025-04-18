package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
    private Label gameOverText;
    private Label restartText;

    public GameOverScreen(MyGame game, Player player) {
        super(game);
        this.player = player;
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        fadeTime = 0;
        gameViewport = new ExtendViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        uiViewport = new FitViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        stage = new Stage(uiViewport, uiBatch);
        font.getData().setScale(1.6f);

        gameOverText = new Label("Game Over", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(gameOverText);
        restartText = new Label("Press SPACE to restart", new Label.LabelStyle(font, Color.WHITE));
        restartText.setFontScale(0.6f);
        stage.addActor(restartText);
    }

    @Override
    public void render(float deltaTime){
        if(game.getLoadState() == MyGame.LoadState.LoadStart){
            renderText();
            fadeToBlack(deltaTime);
            if(resetFadeTimer()){
                game.restart();
                game.setState(MyGame.State.Play);
                game.setScreen(GameScreen.class);
            }
            return;
        }
        ScreenUtils.clear(Color.BLACK);
        if(player.dead()){
            renderText();
            unfadeFromBlack(deltaTime);
            if(fadeTime > fadeDuration && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                fadeTime = 0;
                game.setLoadState(MyGame.LoadState.LoadStart);
            }
        }
        else{
            deathAnimation();
            fadeTime += deltaTime;
            if(fadeTime > fadeDuration)
                player.update(deltaTime);
            if(player.dead())
                fadeTime = 0;
        }
    }

    private void deathAnimation(){
        gameViewport.getCamera().position.set(player.getCenterPos(), 0);
        gameViewport.apply();

        gameBatch.setProjectionMatrix(gameViewport.getCamera().combined);
        gameBatch.begin();
        draw(gameBatch, player.getTexture(), player.drawPos());
        gameBatch.end();
    }

    private void renderText(){
        uiViewport.apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        gameViewport.update(width, height);
        uiViewport.update(width, height, true);

        gameOverText.setAlignment(Align.center);
        gameOverText.setPosition(stage.getWidth()/2, stage.getHeight()/2);
        gameOverText.setSize(1,1);

        restartText.setAlignment(Align.center);
        restartText.setPosition(stage.getWidth()/2, stage.getHeight()/3);
        restartText.setSize(1,1);
    }

    @Override
    public void dispose(){
        stage.dispose();
        font.dispose();
    }
}
