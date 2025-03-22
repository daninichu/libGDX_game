package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.gameObjects.GameObject;

import java.awt.Point;

/**
 * Is for checking and resolving collision between entities.
 */
public class EntityCollisionHandler extends CollisionHandler<CollidableEntity> {
    /**
     * Unlike static objects, entities can move around. The grid must be constantly updated.
     * @param entities The entities with their updated positions.
     */
    public void updateGrid(Array<? extends CollidableEntity> entities) {
        grid.clear();
        for(CollidableEntity entity : entities)
            for(Point cell : getOccupiedCells(entity.locateHurtbox()))
                addToGrid(cell, entity);
    }

    public void handleCollision(CollidableEntity entity) {
        ObjectSet<CollidableEntity> localEntities = getLocalObjects(entity.locateHurtbox());

        for(CollidableEntity localEntity : localEntities)
            if(entity.locateHurtbox().overlaps(localEntity.locateHurtbox()))
                repel(entity, localEntity);
    }

    private static void repel(CollidableEntity e1, CollidableEntity e2) {
        if(e1 instanceof GameObject || e2 instanceof GameObject){
            push(e1, e2);
            return;
        }
        Vector2 direction = e2.getCenterPos().sub(e1.getCenterPos());
        direction.setLength(1/direction.len()).clamp(0, MyGame.TILE_SIZE*1.5f);

        e1.addPos(-direction.x, -direction.y);
        e2.addPos(direction.x, direction.y);
    }

    public static void push(CollidableEntity e1, CollidableEntity e2) {
        Rectangle box1 = e1.locateHurtbox();
        Rectangle box2 = e2.locateHurtbox();

        float overlapX = Math.min(e1.getRightX() - e2.getLeftX(), e2.getRightX() - e1.getLeftX());
        float overlapY = Math.min(e1.getTopY() - e2.getBottomY(), e2.getTopY() - e1.getBottomY());

        if (overlapX < overlapY) {
            // Horizontal push
            if (box1.x < box2.x) {
                e1.addPos(-overlapX / 2f, 0);
                e2.addPos(overlapX / 2f, 0);
            } else {
                e1.addPos(overlapX / 2f, 0);
                e2.addPos(-overlapX / 2f, 0);
            }
        } else {
            // Vertical push
            if (box1.y < box2.y) {
                e1.addPos(0, -overlapY / 2f);
                e2.addPos(0, overlapY / 2f);
            } else {
                e1.addPos(0, overlapY / 2f);
                e2.addPos(0, -overlapY / 2f);
            }
        }
    }
}
