package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.model.entities.Entity;

import java.awt.Point;

/**
 * Is for checking and resolving collision between entities.
 */
public class EntityCollisionHandler extends CollisionHandler<CollidableEntity> {

    public void updateGrid(Array<Entity> entities) {
        grid.clear();
        for (Entity entity : entities) {
            for(Point cell : getOccupiedCells(entity.locateHurtbox())){
                addToGrid(cell, entity);
            }
        }
    }

    public void handleCollisions(CollidableEntity entity) {
        ObjectSet<CollidableEntity> localEntities = getLocalObjects(entity.locateHurtbox());

        for(CollidableEntity localEntity : localEntities){
            if(entity.locateHurtbox().overlaps(localEntity.locateHurtbox())){
                resolveCollision(entity, localEntity);
            }
        }
    }

    public void handleCollision(Array<Entity> entities) {
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            for(int j = 0; j < entities.size-1; j++){
                if(i == j)
                    continue;
                Entity e2 = entities.get(j);
                if(e.locateHurtbox().overlaps(e2.locateHurtbox())){
                    resolveCollision(e, e2);
                }
            }
        }
    }
    public void resolveCollision(CollidableEntity e1, CollidableEntity e2) {
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
