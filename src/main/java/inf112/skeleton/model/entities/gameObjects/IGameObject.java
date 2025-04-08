package inf112.skeleton.model.entities.gameObjects;

import inf112.skeleton.view.ViewableEntity;

public interface IGameObject extends ViewableEntity{
    /**
     * The individual concrete classes will decide what conditions must be fulfilled in
     * order for them to be interactable.
     * @return If the player is interaction range.
     */
    default boolean canInteract() {
        return false;
    }

    default void interact(){}
}
