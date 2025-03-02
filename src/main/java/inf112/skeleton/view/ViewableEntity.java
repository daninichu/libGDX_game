package inf112.skeleton.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface ViewableEntity {
    /**
     * @return A copy of the position.
     */
    Vector2 getPos();

    /**
     * @return A copy of the center position.
     */
    Vector2 getCenterPos();

    Rectangle locateHurtbox();

    float getX();

    float getY();

    float getWidth();

    float getHeight();

    float getCenterX();

    float getCenterY();

    float getLeftX();

    float getRightX();

    float getTopY();

    float getBottomY();

    Texture getTexture();
}
