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

public class PathFinderTest {
    PathFinder pathFinder;
    Point start = new Point(0, 0);

    void tester(Point goal, int expectedDist) {
        Queue<Point> path = pathFinder.findPath(start, goal);
        assertEquals(expectedDist, path.size);
        assertEquals(start, path.first());
        assertEquals(goal, path.last());
    }

    @Test
    void testNoObstacles() {
        HashGrid<Rectangle> grid = new StaticCollisionHandler(new Array<>());
        pathFinder = new PathFinder(grid);

        for(int deg = 0; deg < 360; deg++){
            int x = (int) (100 * MathUtils.cosDeg(deg));
            int y = (int) (100 * MathUtils.sinDeg(deg));
            int minDist = Math.abs(x) + Math.abs(y) + 1;

            tester(new Point(x, y), minDist);
        }
    }

    @Test
    public void testWithObstacles() {
        Array<Rectangle> collisionBoxes = new Array<>();
        collisionBoxes.add(new Rectangle(20, -50, 16, 200));
        HashGrid<Rectangle> grid = new StaticCollisionHandler(collisionBoxes);
        pathFinder = new PathFinder(grid);

        tester(new Point(3, 0), 14);
        tester(new Point(5, 6), 20);
    }
}
