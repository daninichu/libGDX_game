package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.util.Line;

import java.awt.Point;

/**
 * Can efficiently determine which
 * @param <E> The type of objects present in the grid.
 */
public interface HashGrid<E> {
    static int toCellNum(float mapCoords){
        return MathUtils.floor(mapCoords / MyGame.TILE_SIZE);
    }

    static Array<Point> getOccupiedCells(Rectangle r){
        Array<Point> occupiedCells = new Array<>();
        int x1 = toCellNum(r.x);
        int x2 = toCellNum(r.x + r.width);
        int y1 = toCellNum(r.y);
        int y2 = toCellNum(r.y + r.height);
        for(int x = x1; x <= x2; x++)
            for(int y = y1; y <= y2; y++)
                occupiedCells.add(new Point(x, y));
        return occupiedCells;
    }

    static Array<Point> getOccupiedCells(Line l){
        Array<Point> occupiedCells = new Array<>();
        ObjectSet<Point> set = new ObjectSet<>();

        int steps = (int) Math.max(Math.abs(l.dx()), Math.abs(l.dy()));
        float xStep = l.dx() / steps;
        float yStep = l.dy() / steps;
        for (int t = 0; t <= steps; t++) {
            Point cell = new Point(toCellNum(l.x1 + t * xStep), toCellNum(l.y1 + t * yStep));
            if (set.add(cell))
                occupiedCells.add(cell);
        }
        return occupiedCells;
    }

    default ObjectSet<E> getLocalObjects(Rectangle r){
        return getLocalObjects(getOccupiedCells(r));
    }

    default ObjectSet<E> getLocalObjects(Line l){
        return getLocalObjects(getOccupiedCells(l));
    }

    ObjectSet<E> getLocalObjects(Array<Point> cells);
}
