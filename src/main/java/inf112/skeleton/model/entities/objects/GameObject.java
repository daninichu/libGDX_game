package inf112.skeleton.model.entities.objects;

import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.view.ViewableEntity;

public abstract class GameObject extends Entity {

    public GameObject(float x, float y){
        super(x, y);
    }

    @Override
    public void update(float deltaTime){}

    public boolean canInteract(ViewableEntity player) {
        return false;
    }

    public String dialogue(){
        return "";
    }
}
