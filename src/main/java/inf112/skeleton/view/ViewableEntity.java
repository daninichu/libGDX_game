package inf112.skeleton.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Entity;

public interface ViewableEntity {
    /**
     * @return A copy of the position.
     */
    Vector2 getPos();

    /**
     * @return A copy of the center position.
     */
    default Vector2 getCenterPos(){
        return new Vector2(getCenterX(), getCenterY());
    }

    Rectangle locateHurtbox();

    Entity.Direction getDir();

    default float getX(){
        return getPos().x;
    }

    default float getY(){
        return getPos().y;
    }

    float getWidth();

    float getHeight();

    default float getCenterX(){
        return getX() + getWidth()/2;
    }

    default float getCenterY(){
        return getY() + getHeight()/2;
    }

    TextureRegion getTexture();
}
