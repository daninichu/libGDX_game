package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Entity;

import java.awt.Point;

/**
 * Is for checking and resolving collision between entities.
 */
public class EntityCollisionHandler extends CollisionHandler<CollidableEntity> {
    public void updateGrid(Array<Entity> entities) {
        grid.clear();
        for(Entity entity : entities){
            for(Point cell : getOccupiedCells(entity.locateHurtbox()))
                addToGrid(cell, entity);
        }
    }

    public void handleCollision(CollidableEntity entity) {
        ObjectSet<CollidableEntity> localEntities = getLocalObjects(entity.locateHurtbox());

        for(CollidableEntity localEntity : localEntities){
            if(entity.locateHurtbox().overlaps(localEntity.locateHurtbox()))
                repel(entity, localEntity);
        }
    }

    public void handleCollision_n2(Array<Entity> entities) {
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            for(int j = 0; j < entities.size-1; j++){
                if(i == j)
                    continue;
                Entity e2 = entities.get(j);
                if(e.locateHurtbox().overlaps(e2.locateHurtbox())){
                    repel(e, e2);
                }
            }
        }
    }

    private static void repel(CollidableEntity e1, CollidableEntity e2) {
        Vector2 direction = e2.getCenterPos().sub(e1.getCenterPos());
        direction.setLength(1/direction.len()).clamp(0, MyGame.TILE_SIZE);

        e1.addPos(-direction.x, -direction.y);
        e2.addPos(direction.x, direction.y);
    }
}
