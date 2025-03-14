package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.model.entities.Player;

public class Switch extends GameObject {
    private TiledMapTileMapObject door;
    private Rectangle interactionArea;

    public Switch(TiledMapTileMapObject tileObj, Player player){
        super(tileObj, player);
        door = getProperty("Door", TiledMapTileMapObject.class);
        interactionArea = tileRect("Interaction");
        interactionArea.setPosition(pos.x + interactionArea.x, pos.y + interactionArea.y);
    }

    @Override
    public boolean inInteractionRange(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public void interact(){
        MapProperties doorProps = door.getProperties();
        boolean locked = doorProps.get("Locked", boolean.class);
        doorProps.put("Locked", !locked);
        texture.flip(true, false);
    }
}
