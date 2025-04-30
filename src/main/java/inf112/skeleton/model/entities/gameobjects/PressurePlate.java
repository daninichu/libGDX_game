package inf112.skeleton.model.entities.gameobjects;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.collision.CollisionHandler;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.view.FloorEntity;
import inf112.skeleton.view.ViewableEntity;

public class PressurePlate extends GameObject implements FloorEntity{
    private HashGrid<? extends ViewableEntity> grid;
    private Array<MapProperties> links = new Array<>();
    private boolean steppedOn;

    public PressurePlate(TiledMapTileMapObject tileObj, ViewableEntity player, HashGrid<? extends ViewableEntity> grid){
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
        Array<Rectangle> localHurtboxes = new Array<>();
        for(ViewableEntity e : grid.getLocalObjects(locateHurtbox()))
            localHurtboxes.add(e.locateHurtbox());

        if(steppedOn == CollisionHandler.collidesAny(locateHurtbox(), localHurtboxes))
            return;
        steppedOn = !steppedOn;
        for(MapProperties link : links)
            link.put("Locked", !link.get("Locked", boolean.class));
    }
}
