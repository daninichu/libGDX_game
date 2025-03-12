package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.CollidableEntity;
import inf112.skeleton.model.CollisionHandler;
import inf112.skeleton.model.DamageableEntity;
import inf112.skeleton.view.ViewableEntity;

public abstract class Entity implements ViewableEntity, CollidableEntity, DamageableEntity{
    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
    protected TextureRegion texture;
    protected Vector2 pos;
    protected Vector2 prevPos;
    protected Vector2 velocity = new Vector2();
    protected Rectangle hurtbox;
    protected Direction dir = Direction.DOWN;
    public int health;
    protected float speed;
    protected boolean dead;

    public Array<Rectangle> hitboxes = new Array<>();

    public Entity(float x, float y) {
        this.pos = new Vector2(x, y);
        this.prevPos = new Vector2(x, y);
    }

    public void attack(DamageableEntity target) {
        if(CollisionHandler.collidesAny(target, getHitboxes())){
            target.takeDamage(3);
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    public Array<Rectangle> getHitboxes(){
        Array<Rectangle> result = new Array<>();
        for(Rectangle box : hitboxes){
            result.add(new Rectangle(pos.x + box.x, pos.y + box.y, box.width, box.height));
        }
        return result;
    }

    /**
     * Where we decide what this entity should do each frame.
     * @param deltaTime The time interval between each frame.
     */
    public void update(float deltaTime){
        if(health <= 0){
            dead = true;
        }
    }

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
    public Vector2 getPos(){
        return pos.cpy();
    }

    @Override
    public Vector2 getPrevPos(){
        return prevPos.cpy();
    }

    @Override
    public boolean dead(){
        return dead;
    }

    @Override
    public Rectangle locateHurtbox(){
        return new Rectangle(hurtbox.x + pos.x, hurtbox.y + pos.y, hurtbox.width, hurtbox.height);
    }

    @Override
    public Direction getDir(){
        return dir;
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
    public TextureRegion getTexture(){
        return texture;
    }
}
