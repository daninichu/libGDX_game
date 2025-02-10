package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.view.ViewableEntity;

public abstract class Entity implements ViewableEntity{
    protected enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    protected Vector2 pos;
    protected Rectangle hitbox;
    protected Direction dir = Direction.DOWN;
    protected float speed;
    protected float dx, dy;

    public Entity(float x, float y) {
        this.pos = new Vector2(x, y);
    }

    public abstract void update(float deltaTime);

    @Override
    public float getX() {
        return pos.x;
    }

    @Override
    public float getY() {
        return pos.y;
    }

    @Override
    public float getWidth() {
        return hitbox.width;
    }

    @Override
    public float getHeight() {
        return hitbox.height;
    }
}
