package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    private ControllablePlayer player;

    public MyInputProcessor(ControllablePlayer player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode){
        switch(keycode){
            case Input.Keys.A -> player.setLeftMove(true);
            case Input.Keys.D -> player.setRightMove(true);
            case Input.Keys.W -> player.setUpMove(true);
            case Input.Keys.S -> player.setDownMove(true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        switch(keycode){
            case Input.Keys.A -> player.setLeftMove(false);
            case Input.Keys.D -> player.setRightMove(false);
            case Input.Keys.W -> player.setUpMove(false);
            case Input.Keys.S -> player.setDownMove(false);
        }
        return true;
    }

}
