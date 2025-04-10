package inf112.skeleton.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.util.Line;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import com.badlogic.gdx.utils.ObjectSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Leniently tests which cells in a grid are occupied by a line.
 */
public class LineOccupyingCellsTest{
    void scaleToMapCoords(Line line) {
        line.x1 *= MyGame.TILE_SIZE;
        line.x2 *= MyGame.TILE_SIZE;
        line.y1 *= MyGame.TILE_SIZE;
        line.y2 *= MyGame.TILE_SIZE;
    }

    /**
     * Due to the nature of approximations, errors are bound to happen when we try to
     * be precise about exactly which cells are occupied by a line, and which are not.
     * <br>
     * It is therefore necessary to allow {@code HashGrid.getOccupiedCells()} to
     * give approximations that are close enough.
     * <br>
     * For example: (5, 10) is a cell that is actually occupied by the line, but the approximation
     * was evaluating (4.99, 10) got (4, 10) instead.
     * @param l
     * @return A set of cells that are actually occupied by the line, and the neighborhoods of distance 1.
     */
    ObjectSet<Point> bruteForceChecks(Line l) {
        ObjectSet<Point> set = new ObjectSet<>();
        for(int t = 0; t < l.len(); t++){
            float x = l.x1 + l.dx() * t / l.len();
            float y = l.y1 + l.dy() * t / l.len();
            set.add(new Point(HashGrid.toCellNum(x), HashGrid.toCellNum(y)));
            // Neighborhood of cells
            set.add(new Point(HashGrid.toCellNum(x + 1), HashGrid.toCellNum(y)));
            set.add(new Point(HashGrid.toCellNum(x - 1), HashGrid.toCellNum(y)));
            set.add(new Point(HashGrid.toCellNum(x), HashGrid.toCellNum(y + 1)));
            set.add(new Point(HashGrid.toCellNum(x), HashGrid.toCellNum(y - 1)));
        }
        set.add(new Point(HashGrid.toCellNum(l.x2), HashGrid.toCellNum(l.y2)));
        return set;
    }

    void tester(Line line) {
        scaleToMapCoords(line);
        ObjectSet<Point> expectedCells = bruteForceChecks(line);
        Array<Point> actualCells = HashGrid.getOccupiedCells(line);
        for (Point p : actualCells)
            assertTrue(expectedCells.contains(p), p + " is not in " + expectedCells);
    }

    @Test
    void testAroundCircles() {
        float r = 50;
        for(int t = -10; t < 10; t++)
            for(int deg = 0; deg < 360; deg++)
                tester(new Line(t, t, r * MathUtils.cosDeg(deg), r * MathUtils.sinDeg(deg)));
    }
}
