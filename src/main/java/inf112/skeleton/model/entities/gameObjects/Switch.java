package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class Switch extends GameObject {
    private TiledMapTileMapObject door;
    private Box interactionArea;

    public Switch(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj, player);
        door = getProperty("Door", TiledMapTileMapObject.class);
        interactionArea = tileRect("Interaction").addPos(pos);
    }

    @Override
    public boolean inInteractionRange(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public void interact(){
        MapProperties doorProps = door.getProperties();
        Boolean locked = doorProps.get("Locked", boolean.class);
        if(locked == null)
            locked = false;
        doorProps.put("Locked", !locked);
        texture.flip(true, false);
    }
}
