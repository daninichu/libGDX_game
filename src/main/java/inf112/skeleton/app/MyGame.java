package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import inf112.skeleton.controller.MyInputProcessor;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.gameObjects.IDialogue;
import inf112.skeleton.model.entities.gameObjects.IDoor;
import inf112.skeleton.view.UI;
import inf112.skeleton.view.screens.GameScreen;
import inf112.skeleton.view.screens.MainMenuScreen;

import java.util.HashMap;

/**
 * A class that leaves the majority of the work to screens.
 */
public class MyGame extends Game{
    public enum State {
        Title, Play, Dialogue, LoadStart, Loading, LoadEnd
    }
    private State state = State.Title;
    public static int SCREEN_WIDTH = 480*2;
    public static int SCREEN_HEIGHT = 320*2;
    public static int TILE_SIZE = 16;

    private HashMap<String, Screen> screens = new HashMap<>();

    private GameScreen gameScreen;
    private MainMenuScreen mainMenuScreen;

    private MyInputProcessor inputProcessor;
    public UI ui;

    private Player player;
    private Map map;

    @Override
    public void create(){
        player = new Player(0, 0);
//        player = new Player(192+64, 192+32);
        ui = new UI(player);
        map = new Map(player);
//        map.prepareNewMap("grass.tmx");
        map.loadMap("grass.tmx");
        inputProcessor = new MyInputProcessor(this, player);

        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        screens.put("GameScreen", gameScreen);
        screens.put("MainMenuScreen", mainMenuScreen);

        setScreen(mainMenuScreen);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public Map getMap() {
        return map;
    }

    public void changeMap(String mapFile){
        map.loadMap(mapFile);
    }

    public void enterDoor(IDoor door){
        player.setPos(door.getExitPos());
        state = State.LoadStart;
        if(door.getMapFile() != null)
            changeMap(door.getMapFile());
    }

    @Override
    public void render(){
        if(state == State.Loading){
//            changeMap();
        }
        long time = System.nanoTime();
        super.render();
        Gdx.app.log("Render time", (System.nanoTime()-time)/1000000f+" ms");
    }

    public void setDialogue(IDialogue dialogueObj){
        ui.setDialogue(dialogueObj.dialogue());
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
    }

    public void setScreen(String screenClassName){
        setScreen(screens.get(screenClassName));
    }
}
