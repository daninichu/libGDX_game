package inf112.skeleton.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.controller.InputHandler;
import inf112.skeleton.controller.MyInputProcessor;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.ViewableEntity;

/**
 * A class that keeps track of positions of entities.
 */
public class Map {
    private Player player;
    private InputHandler inputHandler;
//    private MyInputProcessor inputHandler;
    private TiledMap tiledMap;

    public Map() {
        this.tiledMap = new TmxMapLoader().load("maps/grassMap.tmx");
        this.player = new Player(0, 0);
        this.inputHandler = new InputHandler(player);
//        this.inputHandler = new MyInputProcessor(player);
//        Gdx.input.setInputProcessor(inputHandler);
    }

    public void update(float deltaTime) {
        inputHandler.handleInput();
        player.update(deltaTime);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public ViewableEntity getPlayer(){
        return player;
    }
}
