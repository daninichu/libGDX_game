package inf112.skeleton.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.MyGame;

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

    public static Line[] getEdges(Rectangle r) {
        return new Line[]{
            new Line(r.x, r.y, r.x + r.width, r.y),
            new Line(r.x + r.width, r.y, r.x + r.width, r.y + r.height),
            new Line(r.x + r.width, r.y + r.height, r.x, r.y + r.height),
            new Line(r.x, r.y + r.height, r.x, r.y)
        };
    }

    public Line[] getEdges() {
        return getEdges(this);
    }

    public static Box cell(){
        return new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
    }
}
