package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;

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
        Vector2 direction = e2.getCenterPos().sub(e1.getCenterPos());
        direction.setLength(1/direction.len()).clamp(0, MyGame.TILE_SIZE*1.5f);

        e1.addPos(-direction.x, -direction.y);
        e2.addPos(direction.x, direction.y);
    }
}
