package inf112.skeleton.model;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.view.ViewableEntity;

public interface CollidableEntity extends ViewableEntity{
    /**
     * @return A copy of the previous position.
     */
    Vector2 getPrevPos();

    void setPos(float x, float y);

    void setPos(Vector2 newPos);
}
