package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.util.Line;

import java.awt.Point;

/**
 * Every cell in the grid keeps track of which object they are occupied by.
 * Can efficiently retrieve objects in specific cells.
 * @param <E> The type of objects present in the grid.
 */
public interface HashGrid<E> {
    static Point toCell(float mapX, float mapY) {
        int cellX = MathUtils.floor(mapX / MyGame.TILE_SIZE);
        int cellY = MathUtils.floor(mapY / MyGame.TILE_SIZE);
        return new Point(cellX, cellY);
    }

    static Vector2 toMapPos(Point cell) {
        return new Vector2(cell.x * MyGame.TILE_SIZE, cell.y * MyGame.TILE_SIZE);
    }

    static Array<Point> getOccupiedCells(Rectangle r){
        Array<Point> occupiedCells = new Array<>();
        Point p1 = toCell(r.x, r.y);
        Point p2 = toCell(r.x + r.width, r.y + r.height);
        for(int x = p1.x; x <= p2.x; x++)
            for(int y = p1.y; y <= p2.y; y++)
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
            Point cell = toCell(l.x1 + t * xStep, l.y1 + t * yStep);
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

    default ObjectSet<E> getLocalObjects(Point... cells){
        return getLocalObjects(new Array<>(cells));
    }

    ObjectSet<E> getLocalObjects(Array<Point> cells);
}
