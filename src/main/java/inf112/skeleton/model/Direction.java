package inf112.skeleton.model;

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

    public Direction opposite() {
        return switch(this){
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }
}
