package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.entities.Player;

public class Door extends GameObject implements IDoor{
    private String mapFile;
    private Vector2 exitPos = new Vector2();
    private Rectangle interactionArea;

    public Door(TiledMapTileMapObject tileObj, Player player) {
        super(tileObj, player);

        TiledMapTileMapObject exitDoor = getProperty("Exit Door", TiledMapTileMapObject.class);
        if (exitDoor == null){
            mapFile = getProperty("Map File", String.class);
            System.out.println(getProperty("id", int.class));
            exitDoor = Map.getObject(mapFile, getProperty("Door ID", int.class));
        }

        exitPos.set(exitDoor.getX(), exitDoor.getY());
        centerExitForPlayer(getProperty("width", float.class));

        interactionArea = tileRect("Interaction");
        interactionArea.setPosition(pos.x + interactionArea.x, pos.y + interactionArea.y);
    }

    /**
     * Adjust the exit position such that the player exits perfectly centered.
     * @param width Width of the tile being occupied by the exit door.
     */
    private void centerExitForPlayer(float width){
        exitPos.add(width/2, 0);
        exitPos.sub(player.getWidth()/2, player.getHeight());
    }

    @Override
    public boolean inInteractionRange(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String cannotOpenMessage(){
        if(getProperty("Locked", boolean.class)){
            return "Door is locked.";
        }
        return null;
    }

    @Override
    public Vector2 getExitPos() {
        return exitPos.cpy();
    }

    @Override
    public String getMapFile() {
        return mapFile;
    }
}
