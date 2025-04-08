package inf112.skeleton.controller;

import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.gameObjects.IGameObject;

public interface ControllablePlayer {
    void setRightMove(boolean t);

    void setLeftMove(boolean t);

    void setUpMove(boolean t);

    void setDownMove(boolean t);

    IGameObject interact(Array.ArrayIterable<? extends IGameObject> objects);

    void attackPressed();
}
