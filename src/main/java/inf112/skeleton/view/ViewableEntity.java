package inf112.skeleton.view;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface ViewableEntity {
    Vector2 getPos();

    Vector2 getCenterPos();

    Rectangle locateHurtbox();

    float getX();

    float getY();

    float getWidth();

    float getHeight();

    float getCenterX();

    float getCenterY();
}
