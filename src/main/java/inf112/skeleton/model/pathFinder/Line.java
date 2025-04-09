package inf112.skeleton.model.pathFinder;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Line2D;

public class Line{
    public float x1, y1, x2, y2;

    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        Line2D line;
//        line.intersectsLine()
    }

    /**
     * Create a line by attaching a vector to a starting point.
     */
    public Line(float startX, float startY, Vector2 vector) {
        this(startX, startY, vector.x, vector.y);
    }

    /**
     * Draw a line from a start position vector to an end position vector.
     */
    public Line(Vector2 start, Vector2 end) {
        this(start.x, start.y, end.x, end.y);
    }

    public float angleDeg(){
        return MathUtils.atan2Deg360(y2 - y1, x2 - x1);
    }

    public Vector2 toVector(){
        return new Vector2(x2 - x1, y2 - y1);
    }

    public float solveForX(float y){
        return x1 + (x2 - x1) * (y - y1) / (y2 - y1);
    }

    public float solveForY(float x){
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }

//    public boolean intersects(Line line) {
//
//    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Line l)
            return x1 == l.x1 && y1 == l.y1 && x2 == l.x2 && y2 == l.y2;
        return false;
    }

    @Override
    public String toString(){
        return "Line{" + "x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + '}';
    }
}
