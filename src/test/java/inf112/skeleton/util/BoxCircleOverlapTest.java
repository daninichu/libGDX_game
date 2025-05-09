package inf112.skeleton.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import inf112.skeleton.util.Box;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Rigorously tests if the overlapping method is working correctly.
 */
public class BoxCircleOverlapTest{
    Box box = new Box(0, 0, 100, 100);

    // TRUE

    String falseNegative(Box box, Circle circle) {
        return "Box " +  box + " should be overlapping circle " + circle;
    }

    void iterateInsideBox(float radius){
        Circle circle = new Circle(0, 0, radius);

        for(int x = 0; x <= 100; x++){
            for(int y = 0; y <= 100; y++){
                circle.setPosition(x, y);
                assertTrue(box.overlaps(circle), falseNegative(box, circle));
            }
        }
    }

    @Test
    void testInside_BIG(){
        iterateInsideBox(1000);
    }

    @Test
    void testInside_MEDIUM(){
        iterateInsideBox(50);
    }

    @Test
    void testInside_SMALL(){
        iterateInsideBox(1);
    }

    void barelyOverlapping(float radius){
        Circle circle = new Circle(0, 0, radius);

        for(int x = 0; x <= 100; x++){
            circle.setPosition(x, -circle.radius * 0.99f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(x, box.height + circle.radius * 0.99f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
        for(int y = 0; y <= 100; y++){
            circle.setPosition(-circle.radius * 0.99f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(box.width + circle.radius * 0.99f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
    }

    @Test
    void testBarelyOverlapping_BIG(){
        barelyOverlapping(1000);
    }

    @Test
    void testBarelyOverlapping_MEDIUM(){
        barelyOverlapping(50);
    }

    @Test
    void testBarelyOverlapping_SMALL(){
        barelyOverlapping(1);
    }

    // FALSE

    String falsePositive(Box box, Circle circle) {
        return "Box " +  box + " should not be overlapping circle " + circle;
    }

    @Test
    void testRightOnEdges(){
        Circle circle = new Circle(0, 0, 100);

        for(int x = -1000; x < 1000; x++){
            circle.setPosition(x, -circle.radius);
            assertFalse(box.overlaps(circle), falsePositive(box, circle));

            circle.setPosition(x, 100 + circle.radius);
            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
        for(int y = -1000; y < 1000; y++){
            circle.setPosition(-circle.radius, y);
            assertFalse(box.overlaps(circle), falsePositive(box, circle));

            circle.setPosition(100 + circle.radius, y);
            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
    }

    @Test
    void testCloseToCorners(){
        Circle circle = new Circle(0, 0, 100);

        for(int deg = 0; deg <= 90; deg++){
            float circleX = 101 + circle.radius * MathUtils.cosDeg(deg);
            float circleY = 101 + circle.radius * MathUtils.sinDeg(deg);
            circle.setPosition(circleX, circleY);

            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
        for(int deg = 90; deg <= 180; deg++){
            float circleX = -1 + circle.radius * MathUtils.cosDeg(deg);
            float circleY = 101 + circle.radius * MathUtils.sinDeg(deg);
            circle.setPosition(circleX, circleY);

            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
        for(int deg = 180; deg <= 270; deg++){
            float circleX = -1 + circle.radius * MathUtils.cosDeg(deg);
            float circleY = -1 + circle.radius * MathUtils.sinDeg(deg);
            circle.setPosition(circleX, circleY);

            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
        for(int deg = 270; deg <= 360; deg++){
            float circleX = 101 + circle.radius * MathUtils.cosDeg(deg);
            float circleY = -1 + circle.radius * MathUtils.sinDeg(deg);
            circle.setPosition(circleX, circleY);

            assertFalse(box.overlaps(circle), falsePositive(box, circle));
        }
    }
}
