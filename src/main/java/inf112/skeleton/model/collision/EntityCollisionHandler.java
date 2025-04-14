package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.ItemDrop;
import inf112.skeleton.model.entities.gameobjects.GameObject;
import inf112.skeleton.view.FloorEntity;

import java.awt.Point;

/**
 * Is for checking and resolving collision between movable entities.
 */
public class EntityCollisionHandler extends CollisionHandler<CollidableEntity> {
    /**
     * Unlike static objects, entities can move around. The grid must be constantly updated.
     * @param entities The entities with their updated positions.
     */
    @Override
    public void updateGrid(Array<? extends CollidableEntity> entities) {
        grid.clear();
        for(CollidableEntity e : entities){
            if(e instanceof ItemDrop || e instanceof FloorEntity || !e.collidable())
                continue;
            for(Point cell : HashGrid.getOccupiedCells(e.locateHurtbox()))
                addToGrid(cell, e);
        }
    }

    @Override
    public void handleCollision(CollidableEntity e) {
        if(e instanceof ItemDrop || e instanceof FloorEntity || !e.collidable())
            return;
        for(CollidableEntity localE : getLocalObjects(e.locateHurtbox())){
            if(e.locateHurtbox().overlaps(localE.locateHurtbox()))
                if(e instanceof GameObject || localE instanceof GameObject)
                    push(e, localE);
                else
                    repel(e, localE);
        }
    }

    private static void repel(CollidableEntity e1, CollidableEntity e2) {
        Vector2 direction = e2.getCenterPos().sub(e1.getCenterPos());
        direction.setLength(1/direction.len()).clamp(0, MyGame.TILE_SIZE*1.5f);

        e1.addPos(-direction.x, -direction.y);
        e2.addPos(direction.x, direction.y);
    }

    private static void push(CollidableEntity e1, CollidableEntity e2) {
        float overlapX = Math.min(e1.getRightX() - e2.getLeftX(), e2.getRightX() - e1.getLeftX());
        float overlapY = Math.min(e1.getTopY() - e2.getBottomY(), e2.getTopY() - e1.getBottomY());

        if (overlapX < overlapY) {
            if (e1.getLeftX() < e2.getLeftX()) {
                e1.addPos(-overlapX / 2f, 0);
                e2.addPos(overlapX / 2f, 0);
            } else {
                e1.addPos(overlapX / 2f, 0);
                e2.addPos(-overlapX / 2f, 0);
            }
        } else {
            if (e1.getBottomY() < e2.getBottomY()) {
                e1.addPos(0, -overlapY / 2f);
                e2.addPos(0, overlapY / 2f);
            } else {
                e1.addPos(0, overlapY / 2f);
                e2.addPos(0, -overlapY / 2f);
            }
        }
    }
}
