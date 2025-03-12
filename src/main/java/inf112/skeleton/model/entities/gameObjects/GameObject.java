package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.ViewableEntity;

public class GameObject extends Entity {
    protected ViewableEntity player;

    public GameObject(TiledMapTileMapObject tileObj, Player player){
        super(tileObj.getX(), tileObj.getY());
        this.player = player;
        TiledMapTile tile = tileObj.getTile();
        this.texture = tile.getTextureRegion();
        this.hurtbox = tileRect(tileObj, "Collision");
    }

    /**
     * Exists for the sake of not having to repeat long statements.
     * @param obj The tile map object.
     * @param s Name of the rectangle object defined in Tiled.
     * @return The rectangle object from Tiled specified by name.
     */
    protected static Rectangle tileRect(TiledMapTileMapObject obj, String s){
        var rectObj = (RectangleMapObject) obj.getTile().getObjects().get(s);
        return rectObj == null? null : new Rectangle(rectObj.getRectangle());
    }

    @Override
    public void update(float deltaTime){}

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
     * @return If the object is interactable by the player.
     */
    public boolean canInteract() {
        return false;
    }
}
