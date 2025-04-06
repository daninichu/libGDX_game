package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.Direction;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.collision.CollidableEntity;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.ViewableEntity;
import inf112.skeleton.view.AnimationHandler;

public abstract class Entity implements ViewableEntity, CollidableEntity, AttackableEntity{
    protected AnimationHandler animation;
    protected TextureRegion texture;
    protected Direction dir = Direction.DOWN;
    protected Vector2 pos;
    protected Vector2 prevPos;
    protected Vector2 velocity = new Vector2();
    protected float speed;

    protected Box hurtbox;
    protected Attack attack = new Attack(){};
    protected int maxHealth;
    protected int health;
    protected float mass;

    public Entity(float x, float y) {
        this.pos = new Vector2(x, y);
        this.prevPos = pos.cpy();
    }

    @Override
    public boolean alreadyHit(AttackableEntity target){
        return attack.alreadyHit(target);
    }

    protected boolean gotHit(AttackableEntity attacker) {
        if(!attacker.alreadyHit(this))
            for(Circle hitbox : attacker.getHitboxes())
                if(locateHurtbox().overlaps(hitbox))
                    return true;
        return false;
    }

    @Override
    public void getAttacked(AttackableEntity attacker) {
        attacker.addHit(this);
        health -= attacker.getDamage();
        velocity.set(attacker.knockbackVector(getCenterPos()));
    }

    @Override
    public void addHit(AttackableEntity target){
        attack.addHit(target);
    }

    @Override
    public int getDamage(){
        return attack.getDamage();
    }

    @Override
    public Vector2 knockbackVector(Vector2 targetPos){
        return attack.knockbackVector(targetPos);
    }

    public Array<Circle> getHitboxes(){
        return new Array<>();
    }

    /**
     * Where we decide what this entity should do each frame.
     * @param deltaTime The time interval between each frame.
     */
    public void update(float deltaTime){
        prevPos.set(pos);
        if(animation != null){
            animation.update(deltaTime);
        }
    }

    /**
     * Adds the velocity vector to the position vector.
     * @param deltaTime Is used to make sure that the change of position is
     * consistent even for different frame rates.
     */
    protected boolean move(float deltaTime){
        pos.add(velocity.cpy().scl(deltaTime));
        return !velocity.isZero();
    }

    /**
     * Change where the entity is facing based on the direction of velocity.
     */
    protected void updateDirection(){
        dir = Direction.fromVector(velocity);
        animation.setDirection(dir);
    }

    public Array<ItemDrop> getItemDrop(){
        return new Array<>();
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
    public Vector2 drawPos(){
        if(animation == null)
            return getPos();
        return getPos().add(animation.getOffset());
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
        return health <= 0;
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
        try{
            return animation.getCurrentFrame();
        } catch(NullPointerException e){
            throw new NullPointerException(e.getMessage() + " in " + getClass().getSimpleName());
        }
    }
}
