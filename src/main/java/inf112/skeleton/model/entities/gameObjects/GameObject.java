package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.ViewableEntity;

public class GameObject extends Entity {
    protected TiledMapTileMapObject tileObj;
    protected ViewableEntity player;

    public GameObject(TiledMapTileMapObject tileObj, Player player){
        super(tileObj.getX(), tileObj.getY());
        this.player = player;
        this.tileObj = tileObj;
        TiledMapTile tile = tileObj.getTile();
        this.texture = tile.getTextureRegion();
        this.hurtbox = tileRect("Collision");
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param s Name of the rectangle object defined in Tiled.
     * @return The rectangle object from Tiled specified by name.
     */
    protected Rectangle tileRect(String s){
        var rectObj = (RectangleMapObject) tileObj.getTile().getObjects().get(s);
        return rectObj == null? null : new Rectangle(rectObj.getRectangle());
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

    @Override
    public void update(float deltaTime){}

    @Override
    public void setPos(Vector2 newPos){
    }

    @Override
    public void setPos(float x, float y){
    }

    @Override
    public void addPos(float x, float y){
    }

    @Override
    public float getCenterX(){
        return locateHurtbox().x + getWidth()/2;
    }

    @Override
    public Rectangle locateHurtbox(){
        return hurtbox == null? null : super.locateHurtbox();
    }

    /**
     * The individual concrete classes will decide what conditions must be fulfilled in
     * order for them to be interactable.
     * @return If the player is interaction range.
     */
    public boolean inInteractionRange() {
        return false;
    }

    public void interact(){}
}
