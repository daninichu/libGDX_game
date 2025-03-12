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
    Array<Rectangle> rectangles = new Array<>();

    /**
     * Fill the grid with collision boxes and assign them to the cells that they occupy.
     * @param collisionBoxes
     */
    public CollisionHandler(Array<Rectangle> collisionBoxes) {
        rectangles.addAll(collisionBoxes);
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
        int x1 = toCellNum(box.x);
        int x2 = toCellNum(box.x + box.width);
        int y1 = toCellNum(box.y);
        int y2 = toCellNum(box.y + box.height);
        for(int x = x1; x <= x2; x++){
            occupiedCells.add(new Point(x, y1));
            occupiedCells.add(new Point(x, y2));
        }
        for(int y = 1 + y1; y < y2; y++){
            occupiedCells.add(new Point(x1, y));
            occupiedCells.add(new Point(x2, y));
        }
        return occupiedCells;
    }

    private static int toCellNum(float mapCoords){
        return (int) (mapCoords / MyGame.TILE_SIZE);
    }

    public void handleCollisions(CollidableEntity entity) {
        ObjectSet<Rectangle> localBoxes = new ObjectSet<>();
//        localBoxes.addAll(rectangles);
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
