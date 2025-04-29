package inf112.skeleton.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class Line implements Shape2D{
    public float x1, y1, x2, y2;

    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Draw a line from a start position vector to an end position vector.
     */
    public Line(Vector2 start, Vector2 end) {
        this(start.x, start.y, end.x, end.y);
    }

    public float dx(){
        return x2 - x1;
    }

    public float dy(){
        return y2 - y1;
    }

    public boolean intersects(Line l) {
        float a = (l.x2 - l.x1) * (l.y1 - y1) - (l.y2 - l.y1) * (l.x1 - x1);
        float b = (l.x2 - l.x1) * (y2 - y1) - (l.y2 - l.y1) * (x2 - x1);
        float c = (x2 - x1) * (l.y1 - y1) - (y2 - y1) * (l.x1 - x1);

        if(b == 0)
            return a == 0;
        return 0 <= a/b && a/b <= 1 && 0 <= c/b && c/b <= 1;
    }

    public boolean intersects(Rectangle r){
        for(Line edge : Box.getEdges(r))
            if(intersects(edge))
                return true;
        return false;
    }

    @Override
    public boolean contains(Vector2 point){
        return false;
    }

    @Override
    public boolean contains(float x, float y){
        return false;
    }

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
