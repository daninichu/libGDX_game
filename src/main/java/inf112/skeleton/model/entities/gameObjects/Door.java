package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.Map;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class Door extends GameObject implements IDoor{
    private String mapFile;
    private Vector2 exitPos = new Vector2();
    private Box interactionArea;

    public Door(TiledMapTileMapObject tileObj, ViewableEntity player) {
        super(tileObj, player);

        TiledMapTileMapObject exitDoor = getProp("Exit Door", TiledMapTileMapObject.class);
        if (exitDoor == null){
            mapFile = getProp("Map File", String.class);
            exitDoor = Map.getObject(mapFile, getProp("Door ID", int.class));
        }
        exitPos.set(exitDoor.getX(), exitDoor.getY());
        centerExitForPlayer(getProp("width", float.class));

        interactionArea = tileRect("Interaction").addPos(pos);

        if(getProp("Locked", boolean.class) == null)
            putProp("Locked", false);
    }

    /**
     * Adjust the exit position such that the player exits perfectly centered.
     * @param width Width of the tile being occupied by the exit door.
     */
    private void centerExitForPlayer(float width){
        exitPos.add(width/2 + player.getX(), player.getY());
        exitPos.sub(player.getLeftX() + player.getWidth()/2, player.getBottomY() + player.getHeight());
    }

    @Override
    public boolean canInteract(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String cannotOpenMessage(){
        return getProp("Locked", boolean.class) ? "Door is locked." : null;
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
