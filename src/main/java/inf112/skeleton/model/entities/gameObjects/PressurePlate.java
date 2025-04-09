package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.view.FloorEntity;
import inf112.skeleton.view.ViewableEntity;

public class PressurePlate extends GameObject implements FloorEntity{
    private HashGrid<? extends ViewableEntity> grid;
    private Array<MapProperties> links = new Array<>();
    private boolean steppedOn;

    public PressurePlate(TiledMapTileMapObject tileObj, ViewableEntity player, HashGrid<? extends ViewableEntity> grid) {
        super(tileObj, player);
        this.grid = grid;

        for(int i = 0; true; i++){
            TiledMapTileMapObject link = getProp("Link"+i, TiledMapTileMapObject.class);
            if(link == null)
                break;
            links.add(link.getProperties());
        }
    }

    @Override
    public void update(float deltaTime){
        if(steppedOn == collidesAny())
            return;
        steppedOn = !steppedOn;
        for(MapProperties link : links)
            link.put("Locked", !link.get("Locked", Boolean.class));
    }

    private boolean collidesAny(){
        Box box = locateHurtbox();
        for(ViewableEntity e : grid.getLocalObjects(locateHurtbox()))
            if(box.overlaps(e.locateHurtbox()))
                return true;
        return false;
    }
}
