package inf112.skeleton.model.entities.gameObjects;

import com.badlogic.gdx.math.Vector2;

public interface IDoor{
    String cannotOpenMessage();

    /**
     * @return What position the player will be in after interacting with the door.
     */
    Vector2 getExitPos();

    /**
     * @return The name of the next map file.
     */
    String getMapFile();
}
