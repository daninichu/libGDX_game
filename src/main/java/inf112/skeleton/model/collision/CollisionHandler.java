package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.util.Line;

import java.awt.Point;

/**
 * Structures the map like a grid. This class is used for resolving collision,
 * in addition to efficiently keeping track of and retrieving collidable objects.
 */
public abstract class CollisionHandler<E> implements HashGrid<E> {
    protected ObjectMap<Point, Array<E>> grid = new ObjectMap<>();

    protected void addToGrid(Point cell, E e){
        if(!grid.containsKey(cell))
            grid.put(cell, new Array<>());
        grid.get(cell).add(e);
    }

    public abstract void updateGrid(Array<? extends E> arr);

    @Override
    public ObjectSet<E> getLocalObjects(Array<Point> cells){
        ObjectSet<E> localObjects = new ObjectSet<>();
        for(Point cell : cells)
            localObjects.addAll(grid.get(cell, new Array<>()));
        return localObjects;
    }

    public static boolean collidesAny(Rectangle box, Iterable<? extends Rectangle> localBoxes) {
        for (Rectangle localBox : localBoxes)
            if(box.overlaps(localBox))
                return true;
        return false;
    }

    public static boolean collidesAny(Line line, Iterable<? extends Rectangle> localBoxes) {
        for (Rectangle localBox : localBoxes)
            if(line.intersects(localBox))
                return true;
        return false;
    }

    public abstract void handleCollision(CollidableEntity entity);
}
