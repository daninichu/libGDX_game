package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.collision.CollidableEntity;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.ViewableEntity;

public abstract class Entity implements ViewableEntity, CollidableEntity, AttackableEntity{
    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
    protected TextureRegion texture;
    protected Vector2 pos;
    protected Vector2 prevPos;
    protected Vector2 velocity = new Vector2();
    protected Box hurtbox;
    protected Direction dir = Direction.DOWN;

    protected Attack attack = new Attack(){};
    protected int health;
    protected float mass;
    protected float speed;
    protected boolean dead;

    public Entity(float x, float y) {
        this.pos = new Vector2(x, y);
        this.prevPos = new Vector2(x, y);
    }

    @Override
    public Attack getAttack(){
        return attack;
    }

    @Override
    public void getAttacked(AttackableEntity attacker) {
        health -= attacker.getAttack().getDamage();
    }

    public Array<Circle> getHitboxes(){
        Array<Circle> result = new Array<>();
        return result;
    }

    /**
     * Where we decide what this entity should do each frame.
     * @param deltaTime The time interval between each frame.
     */
    public void update(float deltaTime){}

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
    public void addPos(float x, float y){
        pos.add(x, y);
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
    public boolean alreadyHit(AttackableEntity target){
        return attack.alreadyHit(target);
    }

    @Override
    public int getHealth(){
        return health;
    }

    @Override
    public float getMass(){
        return mass;
    }

    @Override
    public boolean dead(){
        return dead;
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
    public float getLeftX(){
        return pos.x + hurtbox.x;
    }

    @Override
    public float getRightX(){
        return pos.x + hurtbox.getRightX();
    }

    @Override
    public float getBottomY(){
        return pos.y + hurtbox.y;
    }

    @Override
    public float getTopY(){
        return pos.y + hurtbox.getTopY();
    }

    @Override
    public TextureRegion getTexture(){
        return texture;
    }
}
