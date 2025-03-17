package inf112.skeleton.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoxCircleOverlapTest{
    Box box = new Box(0, 0, 100, 100);

    // TRUE

    String falseNegative(Box box, Circle circle) {
        return "Box " +  box + " should be overlapping circle " + circle;
    }

    @Test
    void testInside_BIG(){
        Circle circle = new Circle(0, 0, 1000);

        for(int x = 0; x <= 100; x++){
            for(int y = 0; y <= 100; y++){
                circle.setPosition(x, y);
                assertTrue(box.overlaps(circle), falseNegative(box, circle));
            }
        }
    }

    @Test
    void testInside_MEDIUM(){
        Circle circle = new Circle(0, 0, 50);

        for(int x = 0; x <= 100; x++){
            for(int y = 0; y <= 100; y++){
                circle.setPosition(x, y);
                assertTrue(box.overlaps(circle), falseNegative(box, circle));
            }
        }
    }

    @Test
    void testInside_SMALL(){
        Circle circle = new Circle(0, 0, 1);

        for(int x = 0; x <= 100; x++){
            for(int y = 0; y <= 100; y++){
                circle.setPosition(x, y);
                assertTrue(box.overlaps(circle), falseNegative(box, circle));
            }
        }
    }

    @Test
    void testBarelyOverlapping_BIG(){
        Circle circle = new Circle(0, 0, 1000);

        for(int x = 0; x <= 100; x++){
            circle.setPosition(x, -circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(x, box.height + circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
        for(int y = 0; y <= 100; y++){
            circle.setPosition(-circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(box.width + circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
    }

    @Test
    void testBarelyOverlapping_MEDIUM(){
        Circle circle = new Circle(0, 0, 50);

        for(int x = 0; x <= 100; x++){
            circle.setPosition(x, -circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(x, box.height + circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
        for(int y = 0; y <= 100; y++){
            circle.setPosition(-circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(box.width + circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
    }

    @Test
    void testBarelyOverlapping_SMALL(){
        Circle circle = new Circle(0, 0, 1);

        for(int x = 0; x <= 100; x++){
            circle.setPosition(x, -circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(x, box.height + circle.radius * 0.9f);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
        for(int y = 0; y <= 100; y++){
            circle.setPosition(-circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));

            circle.setPosition(box.width + circle.radius * 0.9f, y);
            assertTrue(box.overlaps(circle), falseNegative(box, circle));
        }
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
