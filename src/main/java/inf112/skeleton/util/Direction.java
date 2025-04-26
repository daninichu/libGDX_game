package inf112.skeleton.util;

import com.badlogic.gdx.math.Vector2;

public enum Direction{
    RIGHT, LEFT, UP, DOWN;

    public static Direction fromVector(Vector2 vector) {
        float angle = vector.angleDeg();
        if(225 < angle && angle < 315)
            return DOWN;
        else if(135 <= angle && angle <= 225)
            return LEFT;
        else if(45 < angle && angle < 135)
            return UP;
        else
            return RIGHT;
    }

    public static Direction leftOrRight(Vector2 vector) {
        if(90 < vector.angleDeg() && vector.angleDeg() < 270)
            return Direction.LEFT;
        return Direction.RIGHT;
    }

    public Direction opposite() {
        return switch(this){
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }

    /**
     * @return A normalised vector in whatever direction this object is.
     */
    public Vector2 toVector() {
        return switch(this){
            case LEFT -> new Vector2(-1, 0);
            case RIGHT -> new Vector2(1, 0);
            case UP -> new Vector2(0, 1);
            case DOWN -> new Vector2(0, -1);
        };
    }
}
