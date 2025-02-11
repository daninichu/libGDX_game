package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import inf112.skeleton.controller.MyInputProcessor;
import inf112.skeleton.view.screens.GameScreen;

//TODO needs more descriptive name, can decide on group session on wednesday

/**
 * A class that leaves the majority of the work to screens.
 */
public class GdxGame extends Game{
//    private InputProcessor inputProcessor = new MyInputProcessor();

    @Override
    public void create(){
//        Gdx.input.setInputProcessor(inputProcessor);
        setScreen(new GameScreen(this));
    }
}
