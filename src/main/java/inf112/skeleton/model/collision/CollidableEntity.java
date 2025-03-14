package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.view.ViewableEntity;

public interface CollidableEntity extends ViewableEntity{
    void addPos(float x, float y);

    default void addPos(Vector2 vector){
        addPos(vector.x, vector.y);
    }

    /**
     * @return A copy of the previous position.
     */
    Vector2 getPrevPos();

    void setPos(float x, float y);

    default void setPos(Vector2 newPos){
        setPos(newPos.x, newPos.y);
    }
}
