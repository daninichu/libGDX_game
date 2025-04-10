package inf112.skeleton.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Line{
    public float x1, y1, x2, y2;

    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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

    public Vector2 getP1(){
        return new Vector2(x1, y1);
    }

    public Vector2 getP2(){
        return new Vector2(x2, y2);
    }

    public float dx(){
        return x2 - x1;
    }

    public float dy(){
        return y2 - y1;
    }
//
//    public float len(){
//        return Vector2.dst(x1, y1, x2, y2);
//    }
//
//    public float solveForX(float y){
//        return x1 + (dx()) * (y - y1) / (dy());
//    }
//
//    public float solveForY(float x){
//        return y1 + (dy()) * (x - x1) / (dx());
//    }
//
//    public float angleDeg(){
//        return MathUtils.atan2Deg360(dy(), dx());
//    }
//
//    public Vector2 toVector(){
//        return new Vector2(dx(), dy());
//    }
//
//    public Line set(float x1, float y1, float x2, float y2){
//        this.x1 = x1;
//        this.y1 = y1;
//        this.x2 = x2;
//        this.y2 = y2;
//        return this;
//    }
//
//    public Line flip(){
//        return set(x2, y2, x1, y1);
//    }

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
