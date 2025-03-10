package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.gameObjects.Door;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.model.entities.gameObjects.Sign;

public class MyInputProcessor extends InputAdapter {
    private ControllablePlayer player;
    private MyGame game;

    public MyInputProcessor(MyGame game, ControllablePlayer player) {
        this.player = player;
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(game.getState()) {
            case Play -> keyDownPlay(keycode);
            case Dialogue -> keyDownDialogue(keycode);
        }
        return true;
    }

    void keyDownPlay(int keycode) {
        switch (keycode) {
            case Input.Keys.A -> player.setLeftMove(true);
            case Input.Keys.D -> player.setRightMove(true);
            case Input.Keys.W -> player.setUpMove(true);
            case Input.Keys.S -> player.setDownMove(true);
            case Input.Keys.E -> {
                GameObject object = player.interact(game.getMap().getObjects());
                if(object instanceof Door door) {
                    game.changeMap(door);
                }
                else if(object instanceof Sign sign) {
                    game.ui.setDialogue(sign.dialogue());
                    game.setState(MyGame.State.Dialogue);
                }
            }
        }
    }

    void keyDownDialogue(int keycode) {
        switch (keycode) {
            case Input.Keys.E -> {
                game.setState(MyGame.State.Play);
            }
        }
    }


    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A -> player.setLeftMove(false);
            case Input.Keys.D -> player.setRightMove(false);
            case Input.Keys.W -> player.setUpMove(false);
            case Input.Keys.S -> player.setDownMove(false);
        }
        return true;
    }

    void keyUpPlay(int keycode) {
        switch (keycode) {
            case Input.Keys.A -> player.setLeftMove(false);
            case Input.Keys.D -> player.setRightMove(false);
            case Input.Keys.W -> player.setUpMove(false);
            case Input.Keys.S -> player.setDownMove(false);
        }
    }

}
