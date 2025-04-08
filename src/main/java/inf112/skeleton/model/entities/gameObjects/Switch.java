package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class Switch extends GameObject {
    private MapProperties door;
    private Box interactionArea;

    public Switch(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj, player);
        door = getProp("Door", TiledMapTileMapObject.class).getProperties();
        interactionArea = tileRect("Interaction").addPos(pos);
    }

    @Override
    public boolean canInteract(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public void interact(){
        door.put("Locked", !door.get("Locked", boolean.class));
        texture.flip(true, false);
    }
}
