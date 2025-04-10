package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.util.Line;

import java.awt.Point;

/**
 * Structures the map like a grid. Every cell in the grid keeps track of which
 * object they are occupied by.
 */
public abstract class CollisionHandler<E> implements HashGrid<E> {
    protected ObjectMap<Point, Array<E>> grid = new ObjectMap<>();

    protected void addToGrid(Point cell, E e){
        if(!grid.containsKey(cell))
            grid.put(cell, new Array<>());
        grid.get(cell).add(e);
    }
//
//    @Override
//    public ObjectSet<E> getLocalObjects(Rectangle r){
////        ObjectSet<E> localObjects = new ObjectSet<>();
////        for(Point cell : HashGrid.getOccupiedCells(r))
////            localObjects.addAll(grid.get(cell, new Array<>()));
//        return getLocalObjects(HashGrid.getOccupiedCells(r));
//    }
//
//    @Override
//    public ObjectSet<E> getLocalObjects(Line l){
////        ObjectSet<E> localObjects = new ObjectSet<>();
////        for(Point cell : HashGrid.getOccupiedCells(l))
////            localObjects.addAll(grid.get(cell, new Array<>()));
//        return getLocalObjects(HashGrid.getOccupiedCells(l));
//    }

    @Override
    public ObjectSet<E> getLocalObjects(Array<Point> cells){
        ObjectSet<E> localObjects = new ObjectSet<>();
        for(Point cell : cells)
            localObjects.addAll(grid.get(cell, new Array<>()));
        return localObjects;
    }

    public static boolean collidesAny(Rectangle box, Iterable<Rectangle> localBoxes) {
        for (Rectangle localBox : localBoxes)
            if(box.overlaps(localBox))
                return true;
        return false;
    }

    public static boolean collidesAny(Line line, Iterable<Rectangle> localBoxes) {
        for (Rectangle localBox : localBoxes)
            if(line.intersects(localBox))
                return true;
        return false;
    }

    public abstract void handleCollision(CollidableEntity entity);
}
