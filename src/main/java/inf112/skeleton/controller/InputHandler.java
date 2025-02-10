package inf112.skeleton.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Handles more than just player movement, but perhaps inventory selection etc..
 */
public class InputHandler {
    private ControllablePlayer player;

    public InputHandler(ControllablePlayer player) {
        this.player = player;
    }

    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            player.moveRight();
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            player.moveLeft();
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            player.moveUp();
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            player.moveDown();
    }
}
