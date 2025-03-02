package inf112.skeleton.model.entities.objects;

import inf112.skeleton.model.entities.Entity;

public abstract class GameObject extends Entity {
    public GameObject(float x, float y){
        super(x, y);
    }

    @Override
    public void update(float deltaTime){}
}
