package inf112.skeleton.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor{
    ControllablePlayer player;

    public MyInputProcessor(ControllablePlayer player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int i){
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            player.moveRight();
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            player.moveLeft();
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            player.moveUp();
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            player.moveDown();
        return true;
    }

    @Override
    public boolean keyUp(int i){
        return false;
    }

    @Override
    public boolean keyTyped(char c){
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3){
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3){
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3){
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2){
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1){
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1){
        return false;
    }
}
