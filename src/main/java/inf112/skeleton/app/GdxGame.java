package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.view.screens.MainMenuScreen;

//TODO needs more descriptive name, can decide on group session on wednesday

/**
 * A class that leaves the majority of the work to screens.
 */
public class GdxGame extends Game{
    public static float SCREEN_WIDTH = 480;
    public static float SCREEN_HEIGHT = 320;

    @Override
    public void create(){
        setScreen(new MainMenuScreen(this));
    }
}
