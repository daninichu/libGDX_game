package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    public static int SCREEN_WIDTH = 480*2;
    public static int SCREEN_HEIGHT = 320*2;
    public static int TILE_SIZE = 16;

    private GameScreen gameScreen;
    private MainMenuScreen mainMenuScreen;

    private MyInputProcessor inputProcessor;
    public UI ui;

    private Player player;
    private Map map;

    @Override
    public void create(){
        player = new Player(0, 0);
        ui = new UI(player);
        map = new Map("tiledMaps/untitled.tmx", player);
        inputProcessor = new MyInputProcessor(this, player);

        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        setScreen(mainMenuScreen);
        Gdx.input.setInputProcessor(inputProcessor);
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
