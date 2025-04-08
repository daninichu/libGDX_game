package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;

public class Sign extends GameObject implements IDialogue{
    private String text;
    public Box interactionArea;

    public Sign(TiledMapTileMapObject tileObj, ViewableEntity player){
        super(tileObj, player);
        this.text = getProp("Text", String.class);

        interactionArea = tileRect("Interaction").addPos(pos);
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
