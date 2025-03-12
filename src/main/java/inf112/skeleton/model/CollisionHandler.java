package inf112.skeleton.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.view.ViewableEntity;

import java.awt.Point;

/**
 * Structures the map like a grid. Every cell in the grid keeps track of which
 * collision box/boxes they are occupied by.
 */
public class CollisionHandler{
    private ObjectMap<Point, Array<Rectangle>> grid = new ObjectMap<>();

    /**
     * Fill the grid with collision boxes and assign them to the cells that they occupy.
     * @param collisionBoxes
     */
    public CollisionHandler(Array<Rectangle> collisionBoxes) {
        for (Rectangle box : collisionBoxes){
            for(Point cell : getOccupiedCells(box)){
                if(!grid.containsKey(cell))
                    grid.put(cell, new Array<>());
                grid.get(cell).add(box);
            }
        }
    }

    private static Array<Point> getOccupiedCells(Rectangle box){
        int x1 = toCellNum(box.x);
        int x2 = toCellNum(box.x + box.width);
        int y1 = toCellNum(box.y);
        int y2 = toCellNum(box.y + box.height);
        Array<Point> occupiedCells = new Array<>();
        for(int i = x1; i <= x2; i++){
            occupiedCells.add(new Point(i, y1));
            occupiedCells.add(new Point(i, y2));
        }
        for(int i = 1 + y1; i < y2; i++){
            occupiedCells.add(new Point(x1, i));
            occupiedCells.add(new Point(x2, i));
        }
        return occupiedCells;
    }

    private static int toCellNum(float mapCoords){
        return (int) (mapCoords / MyGame.TILE_SIZE);
    }

    public void handleCollisions(CollidableEntity entity) {
        ObjectSet<Rectangle> localBoxes = new ObjectSet<>();
        for(Point cell : getOccupiedCells(entity.locateHurtbox()))
            localBoxes.addAll(grid.get(cell, new Array<>()));

        Vector2 destPos = entity.getPos();
        Vector2 prevPos = entity.getPrevPos();
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
