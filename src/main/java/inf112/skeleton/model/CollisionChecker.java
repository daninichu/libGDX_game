package inf112.skeleton.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.app.MyGame;

import java.awt.Point;

public class CollisionChecker {
    private ObjectMap<Point, Array<Rectangle>> grid = new ObjectMap<>();

    public CollisionChecker(Array<Rectangle> collisionBoxes) {
        for (Rectangle box : collisionBoxes){
            for(Point cell : getCornerCells(box)){
                if(!grid.containsKey(cell))
                    grid.put(cell, new Array<>());
                grid.get(cell).add(box);
            }
        }
    }

    private Array<Point> getCornerCells(Rectangle box) {
        Array<Point> corners = new Array<>();
        corners.add(getGridCell(box.x, box.y));
        corners.add(getGridCell(box.x + box.width, box.y));
        corners.add(getGridCell(box.x, box.y + box.height));
        corners.add(getGridCell(box.x + box.width, box.y + box.height));
        return corners;
    }

    private Point getGridCell(float x, float y) {
        int cellX = (int) (x / MyGame.TILE_SIZE);
        int cellY = (int) (y / MyGame.TILE_SIZE);
        return new Point(cellX, cellY);
    }

    public void checkCollisions(CollidableEntity entity) {
        Rectangle hurtbox = entity.locateHurtbox();
        Array<Rectangle> localBoxes = new Array<>();
        for(Point cell : getCornerCells(hurtbox))
            localBoxes.addAll(grid.get(cell, new Array<>()));
        for (Rectangle box : localBoxes) {
            if (hurtbox.overlaps(box)) {
                handleCollision(entity, box);
                break;
            }
        }
    }

    private void handleCollision(CollidableEntity entity, Rectangle box) {
        Vector2 currentPos = entity.getPos();
        Vector2 prevPos = entity.getPrevPos();
        entity.setPos(prevPos.x, currentPos.y);
        if(entity.locateHurtbox().overlaps(box)){
            entity.setPos(currentPos.x, prevPos.y);
            if(entity.locateHurtbox().overlaps(box))
                entity.setPos(prevPos);
        }
    }
}
