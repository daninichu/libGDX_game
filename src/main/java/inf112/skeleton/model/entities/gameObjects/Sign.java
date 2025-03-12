package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.model.entities.Player;

public class Sign extends GameObject implements IDialogue{
    private String text;
    public Rectangle interactionArea;

    public Sign(TiledMapTileMapObject tileObj, Player player){
        super(tileObj, player);
        this.text = tileObj.getProperties().get("Text", String.class);

        interactionArea = tileRect(tileObj, "Interaction");
        interactionArea.setPosition(pos.x + interactionArea.x, pos.y +  interactionArea.y);
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
