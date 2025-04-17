package inf112.skeleton.controller;

import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.gameobjects.IGameObject;

public interface ControllablePlayer {
    default IGameObject interact(Array.ArrayIterable<? extends IGameObject> objects){
        for(IGameObject object : objects)
            if(object.canInteract())
                return object;
        return null;
    }

    void setRightMove(boolean t);

    void setLeftMove(boolean t);

    void setUpMove(boolean t);

    void setDownMove(boolean t);

    void setShiftPressed(boolean t);

    void attackPressed();
}
