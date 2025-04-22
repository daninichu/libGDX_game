package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import inf112.skeleton.controller.GameInputProcessor;
import inf112.skeleton.controller.InventoryInputProcessor;
import inf112.skeleton.model.LoadZone;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.gameobjects.IBonfire;
import inf112.skeleton.model.entities.gameobjects.IDoor;
import inf112.skeleton.view.screens.GameOverScreen;
import inf112.skeleton.view.screens.GameScreen;
import inf112.skeleton.view.screens.InventoryScreen;
import inf112.skeleton.view.screens.MainMenuScreen;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * A class that leaves the majority of the work to screens.
 */
public class MyGame extends Game{
    public enum State {
        Title, Play, Dialogue, Inventory, GameOver
    }
    public enum LoadState {
        NotLoading, LoadStart, LoadEnd
    }
    private State state = State.Title;
    private LoadState loadState = LoadState.NotLoading;

    public static int SCREEN_WIDTH = 480*2;
    public static int SCREEN_HEIGHT = 320*2;
    public static int TILE_SIZE = 16;

    private ObjectMap<Class<? extends Screen>, Screen> screens = new ObjectMap<>();

    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private InventoryScreen inventoryScreen;
    private GameOverScreen gameOverScreen;

    private InputMultiplexer inputMultiplexer;
    private GameInputProcessor gameProcessor;
    private InventoryInputProcessor inventoryProcessor;

    private Player player;
    private Map map;

    @Override
    public void create(){
        player = new Player(7*32, 2*32);
        map = new Map(this, player);
        loadGame();

        gameProcessor = new GameInputProcessor(this, player);
        inventoryProcessor = new InventoryInputProcessor(this, player);
        inputMultiplexer = new InputMultiplexer(gameProcessor, inventoryProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        gameScreen = new GameScreen(this, player);
        mainMenuScreen = new MainMenuScreen(this);
        inventoryScreen = new InventoryScreen(this, player.getInventory());
        gameOverScreen = new GameOverScreen(this, player);
        screens.put(GameScreen.class, gameScreen);
        screens.put(MainMenuScreen.class, mainMenuScreen);
        screens.put(InventoryScreen.class, inventoryScreen);
        screens.put(GameOverScreen.class, gameOverScreen);

        setScreen(mainMenuScreen);
    }

    public void newGame(){
        SaveData.save(SaveData.defaultSaveData());
        loadGame();
    }

    public void saveGame(IBonfire b){
        SaveData saveData = new SaveData();
        saveData.set(b.getMapFile(), b.spawnX(), b.spawnY(), player.getHp(), player.getMaxHp(), player.getInventory());
        SaveData.save(saveData);
    }

    public void loadGame(){
        SaveData data = SaveData.load();
        player.restart(data.x, data.y, data.hp, data.maxHp, data.inventory);
        map.loadMap(data.mapFile);
    }

    public Map getMap() {
        return map;
    }

    private void changeMap(String mapFile){
        map.loadMap(mapFile);
    }

    public void enterDoor(IDoor door){
        player.setPos(door.getExitPos());
        loadState = LoadState.LoadStart;
        if(door.getMapFile() != null)
            changeMap(door.getMapFile());
    }

    public void enterLoadZone(LoadZone loadZone){
        player.setPos(loadZone.exit);
        player.addPos(-player.getLeftX() + player.getX(), -player.getBottomY() + player.getY());
        player.addPos(-player.getWidth()/2, -player.getHeight()/2);
        loadState = LoadState.LoadStart;
        changeMap(loadZone.mapFile);
    }

    @Override
    public void render(){
        long time = System.nanoTime();
        super.render();
//        Gdx.app.log("Frametime", (System.nanoTime()-time)/1000000f+" ms");
    }

    public void setDialogue(String dialogue){
        gameScreen.setDialogue(dialogue);
    }

    public State getState(){
        return state;
    }

    public LoadState getLoadState(){
        return loadState;
    }

    public void setState(State state){
        this.state = state;
    }

    public void setLoadState(LoadState loadState){
        this.loadState = loadState;
    }

    public void setScreen(Class<? extends Screen> screenClass){
        setScreen(screens.get(screenClass));
    }
}
