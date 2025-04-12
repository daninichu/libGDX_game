package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

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
            for(Point cell : HashGrid.getOccupiedCells(box))
                addToGrid(cell, box);
    }

    public void handleCollision(CollidableEntity e) {
        Vector2 destPos = e.getPos();
        Vector2 prevPos = e.getPrevPos();
        if(destPos.equals(prevPos))
            return;
        ObjectSet<Rectangle> localBoxes = getLocalObjects(e.locateHurtbox());

        if(collidesAny(e.locateHurtbox(), localBoxes)){
            e.setPos(prevPos.x, destPos.y); // Backtrack horizontally, proceed vertically

            if(collidesAny(e.locateHurtbox(), localBoxes)){
                e.setPos(destPos.x, prevPos.y); // Backtrack vertically, proceed horizontally

                if(collidesAny(e.locateHurtbox(), localBoxes))
                    e.setPos(prevPos); // Backtrack altogether
            }
        }
    }
}
