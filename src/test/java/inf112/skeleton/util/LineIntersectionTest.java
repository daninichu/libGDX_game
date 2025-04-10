package inf112.skeleton.util;

import com.badlogic.gdx.math.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineIntersectionTest {

    // Line to Line

    @Test
    void testMovingLine() {
        Line l1 = new Line(-100, -100, 100, 100); // Diagonal
        for(int x = -200; x <= 200; x++){
            Line l2 = new Line(x, -200, x, 200); // Vertical
            if(x < -100)
                assertFalse(l1.intersects(l2), l1 + " should not have intersected " + l2);
            else if(-100 <= x && x <= 100) // Include l1 end point intersecting l2
                assertTrue(l1.intersects(l2), l1 + " should have intersected " + l2);
            else if(100 < x)
                assertFalse(l1.intersects(l2), l1 + " should not have intersected " + l2);
        }
    }

    @Test
    void testEndPoints() {
        Line l1 = new Line(100, -100, 100, 100); // Vertical
        for(int y = -100; y <= 100; y++){
            Line l2 = new Line(100, y, 0, y); // Horizontal
            // l2 end point should intersect l1
            assertTrue(l1.intersects(l2), l1 + " should have intersected " + l2);
        }
    }

    @Test
    void testRotatingLine() {
        Line l1 = new Line(0, 0, 200, 100);
        for(int deg = 0; deg < 360; deg++){
            float x = 100 + MathUtils.cosDeg(deg);
            float y = 50 + MathUtils.sinDeg(deg);
            Line l2 = new Line(x, y, -x, -y);
            assertTrue(l1.intersects(l2), l1 + " should have intersected " + l2);
        }
    }


    // Line to Box

    @Test
    void testLineMovesHorizontally() {
        Box box = new Box(100, 100, 100, 100);
        for(int x = 0; x <= 300; x++){
            Line l = new Line(x, 0, x, 300);
            if(x < 100){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
            else if(100 <= x && x <= 200){
                assertTrue(l.intersects(box), l + " should have intersected " + box);
                assertTrue(box.intersects(l), l + " should have intersected " + box);
            }
            else if(200 < x){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
        }
    }

    @Test
    void testLineMovesVertically() {
        Box box = new Box(100, 100, 100, 100);
        for(int y = 0; y <= 300; y++){
            Line l = new Line(0, y, 300, y);
            if(y < 100){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
            else if(100 <= y && y <= 200){
                assertTrue(l.intersects(box), l + " should have intersected " + box);
                assertTrue(box.intersects(l), l + " should have intersected " + box);
            }
            else if(200 < y){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
        }
    }

    @Test
    void testLineMovesDiagonally() {
        Box box = new Box(100, 100, 100, 100);
        for(int t = 0; t <= 300; t++){
            Line l = new Line(t - 100, t + 100, t + 100, t - 100);
            if(t < 100){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
            else if(100 <= t && t <= 200){
                assertTrue(l.intersects(box), l + " should have intersected " + box);
                assertTrue(box.intersects(l), l + " should have intersected " + box);
            }
            else if(200 < t){
                assertFalse(l.intersects(box), l + " should not have intersected " + box);
                assertFalse(box.intersects(l), l + " should not have intersected " + box);
            }
        }
    }
}
