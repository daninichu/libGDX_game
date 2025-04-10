package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

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

    @Override
    public ObjectSet<E> getLocalObjects(Rectangle box){
        ObjectSet<E> localObjects = new ObjectSet<>();
        for(Point cell : HashGrid.getOccupiedCells(box))
            localObjects.addAll(grid.get(cell, new Array<>()));
        return localObjects;
    }

    public abstract void handleCollision(CollidableEntity entity);
}
