package inf112.skeleton.model.entities.objects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.view.ViewableEntity;

public class Sign extends GameObject {
    private String text;
    private Rectangle interactionArea;

    public Sign(TiledMapTileMapObject tileObj){
        super(tileObj.getX(), tileObj.getY());
        TiledMapTile tile = tileObj.getTile();
        this.texture = tile.getTextureRegion();
        this.hurtbox = new Rectangle(((RectangleMapObject) tile.getObjects().get(0)).getRectangle());
        this.text = tileObj.getProperties().get("Text", String.class);
        interactionArea = locateHurtbox();
        interactionArea.setY(interactionArea.getY() - MyGame.TILE_SIZE*1.5f);
        interactionArea.setHeight(hurtbox.getHeight() + MyGame.TILE_SIZE*1.5f);
    }

    @Override
    public float getCenterX(){
        return locateHurtbox().x + hurtbox.getWidth()/2;
    }

    @Override
    public boolean canInteract(ViewableEntity player) {
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String dialogue(){
        return text;
    }
}
