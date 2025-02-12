package inf112.skeleton.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends AbstractScreen {
    private Stage stage;
    private Viewport viewport = new ExtendViewport(40, 30);
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    private Label title;
    private Label start;

    public MainMenuScreen(Game game){
        super(game);
    }

    @Override
    public void show(){
        super.show();
        stage = new Stage(viewport, batch);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.2f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        title = new Label("Welcome to our game!", labelStyle);
        stage.addActor(title);

        start = new Label("Press SPACE to start", labelStyle);
        start.setFontScale(0.1f);
        stage.addActor(start);
    }

    @Override
    public void render(float deltaTime){
        super.render(deltaTime);
        viewport.apply();
        stage.act(deltaTime);
        stage.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        title.setAlignment(Align.center);
        title.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        title.setSize(1,1);
        start.setAlignment(Align.center);
        start.setPosition(viewport.getWorldWidth()/2, viewport.getWorldHeight()/3);
        start.setSize(1,1);
    }

    @Override
    public void dispose(){
        super.dispose();
        stage.dispose();
        font.dispose();
    }
}
