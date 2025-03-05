package inf112.skeleton.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;

import java.awt.Point;

/**
 * Structures the map like a grid. Every cell in the grid keeps track of which
 * collision box/boxes they are occupied by.
 */
public class CollisionChecker {
    private ObjectMap<Point, Array<Rectangle>> grid = new ObjectMap<>();
    private Array<Rectangle> collisionBoxes = new Array<>();

    /**
     * Fill the grid with collision boxes and assign them to the cells that they occupy.
     * @param collisionBoxes
     */
    public CollisionChecker(Array<Rectangle> collisionBoxes) {
        this.collisionBoxes = collisionBoxes;
        for (Rectangle box : collisionBoxes){
            for(Point cell : getOccupiedCells(box)){
                if(!grid.containsKey(cell))
                    grid.put(cell, new Array<>());
                grid.get(cell).add(box);
            }
        }
    }

    private static Array<Point> getOccupiedCells(Rectangle box){
        Array<Point> occupiedCells = new Array<>();
        for(int i = toCellNum(box.x); i <= toCellNum(box.x + box.width); i++){
            occupiedCells.add(new Point(i, toCellNum(box.y)));
            occupiedCells.add(new Point(i, toCellNum(box.y + box.height)));
        }
        for(int i = 1 + toCellNum(box.y); i < toCellNum(box.y + box.height); i++){
            occupiedCells.add(new Point(toCellNum(box.x), i));
            occupiedCells.add(new Point(toCellNum(box.x + box.width), i));
        }
        return occupiedCells;
    }

    private static int toCellNum(float mapCoords){
        return (int) (mapCoords / MyGame.TILE_SIZE);
    }

    public void checkCollisions(CollidableEntity entity) {
        ObjectSet<Rectangle> localBoxes = new ObjectSet<>();
//        localBoxes.addAll(collisionBoxes);
        for(Point cell : getOccupiedCells(entity.locateHurtbox()))
            localBoxes.addAll(grid.get(cell, new Array<>()));
        for (Rectangle box : localBoxes) {
            if (entity.locateHurtbox().overlaps(box))
                handleCollision(entity, box);
        }
    }

    private void handleCollision(CollidableEntity entity, Rectangle box) {
        Vector2 destPos = entity.getPos();
        Vector2 prevPos = entity.getPrevPos();
        entity.setPos(prevPos.x, destPos.y); // Backtrack horizontally, proceed vertically
        if(entity.locateHurtbox().overlaps(box)){
            entity.setPos(destPos.x, prevPos.y); // Backtrack vertically, proceed horizontally
            if(entity.locateHurtbox().overlaps(box))
                entity.setPos(prevPos); // Backtrack altogether
        }
    }
}
