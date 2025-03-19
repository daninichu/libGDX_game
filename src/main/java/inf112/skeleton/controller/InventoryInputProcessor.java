package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.Inventory;

public class InventoryInputProcessor extends InputAdapter{
    private MyGame game;
    private Inventory inventory;

    public InventoryInputProcessor(MyGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode){
        if(game.getState() != MyGame.State.Inventory)
            return false;
        switch(keycode){
            case Input.Keys.A -> {
                inventory.indexDown();
            }
            case Input.Keys.D -> {
                inventory.indexUp();
            }
        }
        return true;
    }
}
