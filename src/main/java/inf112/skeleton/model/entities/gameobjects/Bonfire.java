package inf112.skeleton.model.entities.gameobjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.ViewableEntity;

public class Bonfire extends GameObject implements IBonfire{
    private Box interactionArea;
    private String mapFile;
    private Vector2 spawnPos;

    public Bonfire(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj, player);

        interactionArea = tileRect("Interaction").addPos(pos);
        mapFile = getProp("Map File", String.class);

        Box spawn = tileRect("Spawn");
        spawnPos = new Vector2(spawn.x, spawn.y).add(pos);
        spawnPos.add(-player.getLeftX() + player.getX(), -player.getBottomY() + player.getY());
        spawnPos.add(-player.getWidth()/2, -player.getHeight()/2);
    }

    @Override
    public boolean canInteract(){
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String getMapFile(){
        return mapFile;
    }

    @Override
    public float spawnX(){
        return spawnPos.x;
    }

    @Override
    public float spawnY(){
        return spawnPos.y;
    }
}
