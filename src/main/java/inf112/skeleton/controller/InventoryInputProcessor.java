package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.IInventoryPlayer;
import inf112.skeleton.model.inventory.Inventory;

public class InventoryInputProcessor extends InputAdapter{
    private MyGame game;
    private IInventoryPlayer player;
    private Inventory inventory;

    public InventoryInputProcessor(MyGame game, IInventoryPlayer player) {
        this.game = game;
        this.player = player;
        this.inventory = player.getInventory();
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
            case Input.Keys.SPACE -> {
                player.useItem(inventory.getItem());
            }
            case Input.Keys.Q -> {
                game.setState(MyGame.State.Play);
            }
        }
        return true;
    }
}
