package inf112.skeleton.model.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.model.collision.StaticCollisionHandler;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathFinderTest {
    Point start = new Point(0, 0);

    int heuristic(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    @Test
    void testNoObstacles() {
        HashGrid<Rectangle> grid = new StaticCollisionHandler(new Array<>());
        PathFinder pathFinder = new PathFinder(grid);

        for(int deg = 0; deg < 360; deg++){
            int x = (int) (100 * MathUtils.cosDeg(deg));
            int y = (int) (100 * MathUtils.sinDeg(deg));
            Point goal = new Point(x, y);
            Queue<Point> path = pathFinder.findPath(start, goal);

            assertEquals(heuristic(start, goal) + 1, path.size);
            assertEquals(start, path.first());
            assertEquals(goal, path.last());
        }
    }

    @Test
    public void testWithObstacles() {
        Array<Rectangle> rectangles = new Array<>();
        rectangles.add(new Rectangle(1, 1, 1, 1));  // Obstacle at (1,1)
        rectangles.add(new Rectangle(2, 2, 1, 1));  // Obstacle at (2,2)
        HashGrid<Rectangle> grid = new StaticCollisionHandler(rectangles);

        PathFinder pathFinder = new PathFinder(grid);

        Point goal = new Point(3, 3);

        Queue<Point> path = pathFinder.findPath(start, goal);

        assertEquals(heuristic(start, goal) + 1, path.size);
        assertEquals(start, path.first());
        assertEquals(goal, path.last());
    }
}
