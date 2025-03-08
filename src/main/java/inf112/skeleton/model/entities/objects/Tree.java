package inf112.skeleton.model.entities.objects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;

public class Tree extends GameObject {
    public Tree(TiledMapTileMapObject tileObj) {
        super(tileObj.getX(), tileObj.getY());
        TiledMapTile tile = tileObj.getTile();
        this.texture = tile.getTextureRegion();
        this.hurtbox = new Rectangle(((RectangleMapObject) tile.getObjects().get(0)).getRectangle());
    }

    @Override
    public float getCenterX(){
        return locateHurtbox().x + hurtbox.getWidth()/2;
    }

}
