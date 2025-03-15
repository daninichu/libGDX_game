package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.view.ViewableEntity;

import java.awt.Point;

/**
 * For checking and resolving collision between moving entities and static objects.
 */
public class StaticCollisionHandler extends CollisionHandler<Rectangle>{
    /**
     * Fill the grid with collision boxes and assign them to the cells that they occupy.
     */
    public StaticCollisionHandler(Array<Rectangle> collisionBoxes) {
        for (Rectangle box : collisionBoxes)
            for(Point cell : getOccupiedCells(box))
                addToGrid(cell, box);
    }

    public void handleCollision(CollidableEntity entity) {
        Vector2 destPos = entity.getPos();
        Vector2 prevPos = entity.getPrevPos();
        if(destPos.equals(prevPos))
            return;

        ObjectSet<Rectangle> localBoxes = getLocalObjects(entity.locateHurtbox());

        if(collidesAny(entity, localBoxes)){
            entity.setPos(prevPos.x, destPos.y); // Backtrack horizontally, proceed vertically

            if(collidesAny(entity, localBoxes)){
                entity.setPos(destPos.x, prevPos.y); // Backtrack vertically, proceed horizontally

                if(collidesAny(entity, localBoxes))
                    entity.setPos(prevPos); // Backtrack altogether
            }
        }
    }

    public static boolean collidesAny(ViewableEntity entity, Iterable<Rectangle> localBoxes) {
        for (Rectangle box : localBoxes)
            if(entity.locateHurtbox().overlaps(box))
                return true;
        return false;
    }
}
