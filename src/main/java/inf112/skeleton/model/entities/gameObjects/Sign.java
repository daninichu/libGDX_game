package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Player;

public class Sign extends GameObject implements IDialogue{
    private String text;
    private Rectangle interactionArea;

    public Sign(TiledMapTileMapObject tileObj, Player player){
        super(tileObj, player);
        this.text = tileObj.getProperties().get("Text", String.class);
        interactionArea = locateHurtbox();
        interactionArea.setY(interactionArea.getY() - MyGame.TILE_SIZE*1.5f);
        interactionArea.setHeight(hurtbox.getHeight() + MyGame.TILE_SIZE*1.5f);
    }

    @Override
    public boolean canInteract() {
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String dialogue(){
        return text;
    }
}
