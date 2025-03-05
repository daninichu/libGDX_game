package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.objects.GameObject;
import inf112.skeleton.model.entities.objects.Sign;
import inf112.skeleton.view.screens.GameScreen;

public class MyInputProcessor extends InputAdapter {
    private ControllablePlayer player;
    private Array<Sign> signs;
    private MyGame game;

    public MyInputProcessor(ControllablePlayer player, MyGame game) {
        this.player = player;
        this.game = game;
        this.signs = game.getMap().getSigns(); // Assuming the Map class has a method to get all Sign objects
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A -> player.setLeftMove(true);
            case Input.Keys.D -> player.setRightMove(true);
            case Input.Keys.W -> player.setUpMove(true);
            case Input.Keys.S -> player.setDownMove(true);
            case Input.Keys.SPACE -> {
                if(game.getGameScreen().getState() == GameScreen.State.Dialogue){
                    game.getGameScreen().setState(GameScreen.State.Play);
                }
                else if(player.interact(game.ui)){
                    game.getGameScreen().setState(GameScreen.State.Dialogue);
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A -> player.setLeftMove(false);
            case Input.Keys.D -> player.setRightMove(false);
            case Input.Keys.W -> player.setUpMove(false);
            case Input.Keys.S -> player.setDownMove(false);
            case Input.Keys.E -> {
//                game.getGameScreen().hideSignText();
            }
        }
        return true;
    }
}
