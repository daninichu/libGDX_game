package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class Door extends GameObject implements IDoor{
    private Vector2 exitPos = new Vector2();
    private String nextMap;
    private Rectangle interactionArea;

    public Door(TiledMapTileMapObject tileObj, Player player) {
        super(tileObj, player);
        MapProperties properties = tileObj.getProperties();

        TiledMapTileMapObject exitDoor = properties.get("Exit Door", TiledMapTileMapObject.class);
        if (exitDoor != null)
            exitPos.set(exitDoor.getX(), exitDoor.getY());
        else {
            float x = properties.get("New x", Float.class);
            float y = properties.get("New y", Float.class);
            exitPos.set(x, y);
        }
        float width = properties.get("width", Float.class);
        exitPos.add(width/2, 0);
        exitPos.sub(player.getWidth()/2, player.getHeight());

        nextMap = properties.get("Next Map", String.class);

        interactionArea = locateHurtbox();
        interactionArea.setY(interactionArea.getY() - MyGame.TILE_SIZE*1.5f);
        interactionArea.setHeight(hurtbox.getHeight() + MyGame.TILE_SIZE*1.5f);
    }

    @Override
    public boolean canInteract(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public Vector2 getExitPos() {
        return exitPos;
    }

    @Override
    public String getNextMap() {
        return nextMap;
    }
}
