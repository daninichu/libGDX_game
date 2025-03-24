package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.ViewableEntity;

public class Sign extends GameObject implements IDialogue{
    private String text;
    public Box interactionArea;

    public Sign(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj, player);
        this.text = getProperty("Text", String.class);

        interactionArea = tileRect("Interaction");
        interactionArea.addPos(pos);
    }

    @Override
    public boolean inInteractionRange() {
        return interactionArea.contains(player.getCenterPos());
    }

    @Override
    public String dialogue(){
        return text;
    }
}
