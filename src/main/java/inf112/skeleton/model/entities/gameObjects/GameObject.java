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
        this.hurtbox = new Rectangle(((RectangleMapObject) tile.getObjects().get(0)).getRectangle());
    }

    @Override
    public void update(float deltaTime){}

    @Override
    public float getCenterX(){
        return locateHurtbox().x + getWidth()/2;
    }

    public boolean canInteract() {
        return false;
    }

    public String dialogue(){
        return "";
    }
}
