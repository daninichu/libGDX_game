package inf112.skeleton.model.states;

import inf112.skeleton.model.entities.Entity;

public interface IState<E extends Entity> {
    void update(E entity, float deltaTime);
}
