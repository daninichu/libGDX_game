package inf112.skeleton.controller;

import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.gameObjects.GameObject;

public interface ControllablePlayer {
    void setRightMove(boolean t);

    void setLeftMove(boolean t);

    void setUpMove(boolean t);

    void setDownMove(boolean t);

    GameObject interact(Array.ArrayIterable<GameObject> objects);

    void attackPressed();
}
