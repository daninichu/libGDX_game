package inf112.skeleton.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Can detect overlap with circles.
 */
public class Box extends Rectangle{
    public Box(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public Box(Rectangle r) {
        super(r);
    }

    public float getRightX() {
        return x + width;
    }

    public float getTopY() {
        return y + height;
    }

    public float getCenterX() {
        return x + width / 2;
    }

    public float getCenterY() {
        return y + height / 2;
    }

    public Vector2 getCenter(){
        return new Vector2(getCenterX(), getCenterY());
    }

    public Box addPos(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Box addPos(Vector2 pos) {
        return addPos(pos.x, pos.y);
    }

    public boolean overlaps(Circle circle) {
        float clampX = Math.max(x, Math.min(circle.x, getRightX()));
        float clampY = Math.max(y, Math.min(circle.y, getTopY()));
        return Vector2.dst(circle.x, circle.y, clampX, clampY) < circle.radius;
    }

    public boolean intersects(Line l) {
        return l.intersects(this);
    }

    public Line[] getEdges() {
        return new Line[]{
            new Line(x, y, getRightX(), y),
            new Line(getRightX(), y, getRightX(), getTopY()),
            new Line(getRightX(), getTopY(), x, getTopY()),
            new Line(x, getTopY(), x, y)
        };
    }
}
