package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.MyGame;

public class MainMenuScreen extends AbstractScreen {
    private Stage stage;
    private Label title;
    private Label start;

    public MainMenuScreen(MyGame game){
        super(game);
        fadeDuration = 1;
    }

    @Override
    public void show(){
        super.show();
        uiViewport = new FitViewport(200, 150);
        stage = new Stage(uiViewport, uiBatch);
        font.getData().setScale(1);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        title = new Label("Welcome to my game!", labelStyle);
        stage.addActor(title);

        start = new Label("Press SPACE to start", labelStyle);
        start.setFontScale(0.5f);
        stage.addActor(start);
    }

    @Override
    public void render(float deltaTime){
        ScreenUtils.clear(Color.CLEAR);
        uiViewport.apply();
        stage.act();
        stage.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setLoadState(MyGame.LoadState.LoadStart);
        }
        if(game.getLoadState() == MyGame.LoadState.LoadStart){
            fadeToBlack(deltaTime);
            if(resetFadeTimer()){
                game.setState(MyGame.State.Play);
                game.setScreen(GameScreen.class);
            }
        }
    }

    @Override
    public void resize(int width, int height){
        uiViewport.update(width, height);

        title.setAlignment(Align.center);
        title.setPosition(uiViewport.getWorldWidth()/2, uiViewport.getWorldHeight()/2);
        title.setSize(1,1);

        start.setAlignment(Align.center);
        start.setPosition(uiViewport.getWorldWidth()/2, uiViewport.getWorldHeight()/3);
        start.setSize(1,1);
    }

    @Override
    public void dispose(){
        super.dispose();
        stage.dispose();
    }
}
