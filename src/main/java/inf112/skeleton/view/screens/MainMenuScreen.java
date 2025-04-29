package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.MyGame;

public class MainMenuScreen extends AbstractScreen {
    private Stage stage2;
    private boolean areYouSureClicked;

    public MainMenuScreen(MyGame game){
        super(game);
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        uiViewport = new FitViewport(200, 150);
        font.getData().setScale(1);

        stage = new Stage(uiViewport, uiBatch);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Welcome to my game!", labelStyle);
        Label start = new Label("Press SPACE to load game", labelStyle);
        start.setFontScale(0.5f);
        Label newGame = new Label("Press ENTER to start new game", labelStyle);
        newGame.setFontScale(0.5f);
        table.add(title).pad(10).row();
        table.add(newGame).pad(8).row();
        table.add(start);

        stage2 = new Stage(uiViewport, uiBatch);
        table = new Table();
        table.setFillParent(true);
        stage2.addActor(table);
        Label areYouSure = new Label("""
            Are you sure you want to start a new game?
            This cannot be undone.

            Press BACKSPACE to go back

            Press ENTER to start new game""", labelStyle
        );
        areYouSure.setFontScale(0.4f);
        table.add(areYouSure);
    }

    @Override
    public void render(float deltaTime){
        ScreenUtils.clear(Color.CLEAR);
        uiViewport.apply();
        if(areYouSureClicked)
            renderAreYouSure();
        else
            renderLoadOrNewGame();
        if(game.getLoadState() == MyGame.LoadState.LoadStart){
            fadeToBlack(deltaTime);
            if(resetFadeTimer()){
                game.setState(MyGame.State.Play);
                game.setScreen(GameScreen.class);
            }
        }
    }

    private void renderLoadOrNewGame(){
        stage.act();
        stage.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            areYouSureClicked = true;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setLoadState(MyGame.LoadState.LoadStart);
            game.loadGame();
        }
    }

    private void renderAreYouSure(){
        stage2.act();
        stage2.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE))
            areYouSureClicked = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setLoadState(MyGame.LoadState.LoadStart);
            game.newGame();
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        stage2.dispose();
    }
}
