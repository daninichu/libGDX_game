package inf112.skeleton.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.util.Direction;
import inf112.skeleton.util.Box;

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

    Vector2 drawPos();

    boolean dead();

    default Box locateHurtbox(){
        return new Box(getLeftX(), getBottomY(), getWidth(), getHeight());
    }

    Direction getDir();

    default float getX(){
        return getPos().x;
    }

    default float getY(){
        return getPos().y;
    }

    float getWidth();

    float getHeight();

    default float getCenterX(){
        return getLeftX() + getWidth()/2;
    }

    default float getCenterY(){
        return getBottomY() + getHeight()/2;
    }

    /**
     * @return The x-coordinate of the left side of the hurtbox.
     */
    float getLeftX();

    /**
     * @return The x-coordinate of the right side of the hurtbox.
     */
    float getRightX();

    /**
     * @return The y-coordinate of the bottom of the hurtbox.
     */
    float getBottomY();

    /**
     * @return The y-coordinate of the top of the hurtbox.
     */
    float getTopY();

    TextureRegion getTexture();

    int getHp();

    int getMaxHp();
}
