package inf112.skeleton.model.entities.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class Sign extends GameObject {
    private boolean interacting;
    private String text;

    public Sign(TiledMapTileMapObject tileObj){
        super(tileObj.getX(), tileObj.getY());
        this.texture = new Texture("maps/sign.png");
        this.hurtbox = new Rectangle(((RectangleMapObject) tileObj.getTile().getObjects().get(0)).getRectangle());
        interactionCircle = new Circle(locateHurtbox().getCenter(new Vector2()),MyGame.TILE_SIZE*1.5f);
        this.text = tileObj.getProperties().get("Text", String.class);
    }

//    public boolean canInteract(Player player) {
//        return interactionCircle.contains(player.getCenterPos());
//    }

    public void setInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    public boolean interacting() {
        return interacting;
    }

    @Override
    public String dialogue(){
        return text;
    }
}
