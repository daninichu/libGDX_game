package inf112.skeleton.model.entities.objects;

import com.badlogic.gdx.math.Circle;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;

public abstract class GameObject extends Entity {
    protected Circle interactionCircle;

    public GameObject(float x, float y){
        super(x, y);
    }

    @Override
    public void update(float deltaTime){}

    public boolean canInteract(Player player) {
        return interactionCircle.contains(player.getCenterPos());
    }

    public String dialogue(){
        return "";
    }
}
