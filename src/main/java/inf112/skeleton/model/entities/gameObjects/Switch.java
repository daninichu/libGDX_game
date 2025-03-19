package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.util.Box;

public class Switch extends GameObject {
    private TiledMapTileMapObject door;
    private Box interactionArea;

    public Switch(TiledMapTileMapObject tileObj, Player player){
        super(tileObj, player);
        door = getProperty("Door", TiledMapTileMapObject.class);
        interactionArea = tileRect("Interaction");
        interactionArea.addPos(pos);
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
