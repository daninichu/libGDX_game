package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.CollidableEntity;
import inf112.skeleton.view.ViewableEntity;

public abstract class Entity implements ViewableEntity, CollidableEntity{
    protected enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
    protected Vector2 pos;
    protected Vector2 prevPos;
    protected Vector2 velocity = new Vector2();
    protected Rectangle hurtbox;
    protected Direction dir = Direction.DOWN;
    protected float speed;

    public Entity(float x, float y) {
        this.pos = new Vector2(x, y);
        this.prevPos = new Vector2(x, y);
    }

    /**
     * Where we decide what this entity should do each frame.
     * @param deltaTime The time interval between each frame.
     */
    public abstract void update(float deltaTime);

    /**
     * Adds the velocity vector to the position vector.
     * @param deltaTime Is used to make sure that the change of position is
     * consistent even for different frame rates.
     */
    protected boolean move(float deltaTime){
        if(velocity.len() == 0)
            return false;
        updateDirection();
        pos.add(velocity.cpy().scl(deltaTime));
        return true;
    }

    /**
     * Change where the entity is facing based on the direction of velocity.
     */
    private void updateDirection(){
        float angle = velocity.angleDeg();
        if(225 < angle && angle < 315)
            dir = Direction.DOWN;
        else if(135 <= angle && angle <= 225)
            dir = Direction.LEFT;
        else if(45 < angle && angle < 135)
            dir = Direction.UP;
        else
            dir = Direction.RIGHT;
    }

    @Override
    public void setPos(float x, float y){
        pos.set(x, y);
    }

    @Override
    public void setPos(Vector2 newPos){
        pos.set(newPos);
    }

    @Override
    public Vector2 getPos(){
        return pos.cpy();
    }

    public Vector2 getPrevPos(){
        return prevPos.cpy();
    }

    @Override
    public Vector2 getCenterPos(){
        return new Vector2(getCenterX(), getCenterY());
    }

    @Override
    public Rectangle locateHurtbox(){
        return new Rectangle(hurtbox.x + pos.x, hurtbox.y + pos.y, hurtbox.width, hurtbox.height);
    }

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
        return hurtbox.width;
    }

    @Override
    public float getHeight() {
        return hurtbox.height;
    }

    @Override
    public float getCenterX(){
        return pos.x + hurtbox.width/2;
    }

    @Override
    public float getCenterY(){
        return pos.y + hurtbox.height/2;
    }

    @Override
    public float getLeftX(){
        return locateHurtbox().x;
    }

    @Override
    public float getRightX(){
        return locateHurtbox().x + hurtbox.width;
    }

    @Override
    public float getTopY(){
        return locateHurtbox().y;
    }

    @Override
    public float getBottomY(){
        return locateHurtbox().y + hurtbox.height;
    }
}
