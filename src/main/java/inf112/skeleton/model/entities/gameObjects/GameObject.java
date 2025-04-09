package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class GameObject extends Entity implements IGameObject{
    protected TiledMapTileMapObject tileObj;
    protected ViewableEntity player;

    public GameObject(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj.getX(), tileObj.getY());
        health = 1;
        this.tileObj = tileObj;
        this.player = player;
        this.texture = tileObj.getTile().getTextureRegion();
        this.hurtbox = tileRect("Collision");
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param s Name of the rectangle object defined in Tiled.
     * @return The rectangle object from Tiled specified by name.
     */
    protected Box tileRect(String s){
        var rectObj = (RectangleMapObject) tileObj.getTile().getObjects().get(s);
        if(rectObj == null)
            return new Box(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        return new Box(rectObj.getRectangle());
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param key Name of the property of the tile object.
     * @param type The type of the desired property.
     * @return The property of the tile object specified by name.
     */
    protected <T> T getProp(String key, Class<T> type){
        return tileObj.getProperties().get(key, type);
    }

    protected <T> T getTileProp(String key, Class<T> type){
        return tileObj.getTile().getProperties().get(key, type);
    }

    protected void putProp(String key, Object val){
        tileObj.getProperties().put(key, val);
    }

    public boolean movable() {
        return getTileProp("Movable", boolean.class) != null;
    }

    @Override
    public boolean collidable(){
        return getTileProp("Not Collidable", boolean.class) == null;
    }

    @Override
    public TextureRegion getTexture(){
        return texture;
    }
}
