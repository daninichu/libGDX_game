package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.screens.GameScreen;

//TODO needs more descriptive name, can decide on group session on wednesday

/**
 * A class that leaves the majority of the work to screens.
 */
public class MyGame extends Game{
    public static float SCREEN_WIDTH = 480;
    public static float SCREEN_HEIGHT = 320;

    private Player player;
    private Map map;

    @Override
    public void create(){
        player = new Player(0, 0);
        map = new Map("maps/grassMap.tmx", player);
        setScreen(new GameScreen(this, map));
    }
}
