package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class GameOverScreen extends AbstractScreen{
    private Player player;

    public GameOverScreen(MyGame game, Player player) {
        super(game);
        this.player = player;
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        fadeTime = 0;
        font.getData().setScale(1);
        gameViewport = new ExtendViewport(GameScreen.VIEW_WIDTH, GameScreen.VIEW_HEIGHT);
        uiViewport = new FitViewport(200, 150);
        stage = new Stage(uiViewport, uiBatch);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label gameOverText = new Label("Game Over", labelStyle);
        table.add(gameOverText).pad(10).row();

        Label loadSave = new Label("Press SPACE to reload from last save", labelStyle);
        loadSave.setFontScale(0.5f);
        table.add(loadSave);
    }

    @Override
    public void render(float deltaTime){
        if(game.getLoadState() == MyGame.LoadState.LoadStart){
            renderText();
            fadeToBlack(deltaTime);
            if(resetFadeTimer()){
                game.loadGame();
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
    public void dispose(){
        super.dispose();
        stage.dispose();
    }
}
