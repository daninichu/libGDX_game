package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import inf112.skeleton.controller.MyInputProcessor;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.screens.GameScreen;
import inf112.skeleton.view.screens.MainMenuScreen;

/**
 * A class that leaves the majority of the work to screens.
 */
public class MyGame extends Game{
    public enum State {
        Play, Pause
    }
    private State state = State.Play;
    public static int SCREEN_WIDTH = 480*2;
    public static int SCREEN_HEIGHT = 320*2;
    public static int TILE_SIZE = 32/2;

    private GameScreen gameScreen;
    private MainMenuScreen mainMenuScreen;

    private MyInputProcessor inputProcessor;
    public UI ui;

    private Player player;
    private Map map;

    @Override
    public void create(){
        player = new Player(0, 0);
        ui = new UI();
        map = new Map("maps/untitled.tmx", player);
        inputProcessor = new MyInputProcessor(player, this);

        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        setScreen(mainMenuScreen);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(){
        switch(state){
            case Play -> super.render();
            default -> {}
        }
    }

    public void escPressed(){
        switch(state){
            case Play -> {state = State.Pause;}
            case Pause -> {state = State.Play;}
        }
    }

    public void switchState(State state){
        this.state = state;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public Map getMap() {
        return map;
    }
}
