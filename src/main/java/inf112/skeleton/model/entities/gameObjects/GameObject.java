package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class GameObject extends Entity {
    protected TiledMapTileMapObject tileObj;
    protected ViewableEntity player;

    public GameObject(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj.getX(), tileObj.getY());
        mass = 1;
        this.tileObj = tileObj;
        this.player = player;
        TiledMapTile tile = tileObj.getTile();
        this.texture = tile.getTextureRegion();
        this.hurtbox = tileRect("Collision");
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param s Name of the rectangle object defined in Tiled.
     * @return The rectangle object from Tiled specified by name.
     */
    protected Box tileRect(String s){
        var rectObj = (RectangleMapObject) tileObj.getTile().getObjects().get(s);
        if(rectObj == null){
            return new Box(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
//            return new Box(0,0,0,0);
        }
        return new Box(rectObj.getRectangle());
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param key Name of the property of the tile object.
     * @param type The type of the desired property.
     * @return The property of the tile object specified by name.
     */
    protected <T> T getProperty(String key, Class<T> type){
        return tileObj.getProperties().get(key, type);
    }

    /**
     * The individual concrete classes will decide what conditions must be fulfilled in
     * order for them to be interactable.
     * @return If the player is interaction range.
     */
    public boolean inInteractionRange() {
        return false;
    }

    public boolean moveable() {
        return tileObj.getTile().getProperties().get("Moveable", boolean.class) != null;
    }

    public void interact(){}

}
