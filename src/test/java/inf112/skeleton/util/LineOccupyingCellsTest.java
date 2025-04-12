package inf112.skeleton.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.collision.HashGrid;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import com.badlogic.gdx.utils.ObjectSet;

import static org.junit.jupiter.api.Assertions.*;

public class LineOccupyingCellsTest{
    ObjectSet<Point> bruteForceChecks(Line l) {
        ObjectSet<Point> set = new ObjectSet<>();
        Point end = HashGrid.toCell(l.x2, l.y2);
        for(float t = 0; true; t += 0.0001f) {
            float x = l.x1 + l.dx() * t;
            float y = l.y1 + l.dy() * t;
            Point cell = HashGrid.toCell(x, y);
            set.add(cell);
            if(cell.equals(end))
                break;
        }
        return set;
    }

    @Test
    void testAroundCircles() {
        float r = 400;
        for(int deg = 0; deg < 360; deg++){
            Line line = new Line(0, 0, r * MathUtils.cosDeg(deg), r * MathUtils.sinDeg(deg));
            ObjectSet<Point> expectedCells = bruteForceChecks(line);
            Array<Point> actualCells = HashGrid.getOccupiedCells(line);

            for (Point p : actualCells)
                assertTrue(expectedCells.contains(p), p + " is not in " + expectedCells);
        }
    }
}
