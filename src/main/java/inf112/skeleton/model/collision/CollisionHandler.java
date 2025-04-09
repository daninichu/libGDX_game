package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;

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

    protected static Array<Point> getOccupiedCells(Rectangle box){
        Array<Point> occupiedCells = new Array<>();
        int x1 = toCellNum(box.x);
        int x2 = toCellNum(box.x + box.width);
        int y1 = toCellNum(box.y);
        int y2 = toCellNum(box.y + box.height);
        for(int x = x1; x <= x2; x++)
            for(int y = y1; y <= y2; y++)
                occupiedCells.add(new Point(x, y));
        return occupiedCells;
    }

    protected static int toCellNum(float mapCoords){
        return MathUtils.floor(mapCoords / MyGame.TILE_SIZE);
    }

    @Override
    public ObjectSet<E> getLocalObjects(Rectangle box){
        ObjectSet<E> localObjects = new ObjectSet<>();
        for(Point cell : getOccupiedCells(box))
            localObjects.addAll(grid.get(cell, new Array<>()));
        return localObjects;
    }
}
