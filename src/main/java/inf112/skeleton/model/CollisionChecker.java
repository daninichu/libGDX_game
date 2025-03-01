package inf112.skeleton.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.app.MyGame;

import java.awt.Point;

public class CollisionChecker {
    private ObjectMap<Point, Array<Rectangle>> grid = new ObjectMap<>();

    public CollisionChecker(Array<Rectangle> collisionBoxes) {
        for (Rectangle box : collisionBoxes){
            for(int i = toCellNum(box.x); i <= toCellNum(box.x + box.width); i++){
                Point bottomCell = new Point(i, toCellNum(box.y));
                Point topCell = new Point(i, toCellNum(box.y + box.height));
                addToGrid(box, bottomCell);
                addToGrid(box, topCell);
            }
            for(int i = toCellNum(box.y); i <= toCellNum(box.y + box.height); i++){
                Point leftCell = new Point(toCellNum(box.x), i);
                Point rightCell = new Point(toCellNum(box.x + box.width), i);
                addToGrid(box, leftCell);
                addToGrid(box, rightCell);
            }
        }
    }

    private void addToGrid(Rectangle box, Point cell){
        if(!grid.containsKey(cell))
            grid.put(cell, new Array<>());
        grid.get(cell).add(box);
    }

    private static Array<Point> getCornerCells(Rectangle box) {
        Array<Point> corners = new Array<>();
        corners.add(getGridCell(box.x, box.y));
        corners.add(getGridCell(box.x + box.width, box.y));
        corners.add(getGridCell(box.x, box.y + box.height));
        corners.add(getGridCell(box.x + box.width, box.y + box.height));
        return corners;
    }

    private static int toCellNum(float mapCoords){
        return (int) (mapCoords / MyGame.TILE_SIZE);
    }

    private static Point getGridCell(float mapX, float mapY) {
        return new Point(toCellNum(mapX), toCellNum(mapY));
    }

    public void checkCollisions(CollidableEntity entity) {
        Rectangle hurtbox = entity.locateHurtbox();
        ObjectSet<Rectangle> localBoxes = new ObjectSet<>();
        for(Point cell : getCornerCells(hurtbox))
            localBoxes.addAll(grid.get(cell, new Array<>()));
        for (Rectangle box : localBoxes) {
            if (hurtbox.overlaps(box))
                handleCollision(entity, box);
        }
    }

    private void handleCollision(CollidableEntity entity, Rectangle box) {
        Vector2 destPos = entity.getPos();
        Vector2 prevPos = entity.getPrevPos();
        entity.setPos(prevPos.x, destPos.y); // Backtrack horizontally, proceed vertically
        if(entity.locateHurtbox().overlaps(box)){
            entity.setPos(destPos.x, prevPos.y); // Backtrack vertically, proceed horizontally
            if(entity.locateHurtbox().overlaps(box))
                entity.setPos(prevPos); // Backtrack altogether
        }
    }
}
