package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.util.Direction;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.collision.CollidableEntity;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.AnimationHandler;

public abstract class Entity implements CollidableEntity, AttackableEntity{
    protected AnimationHandler animation;
    protected TextureRegion texture;
    protected Direction dir = Direction.DOWN;
    protected Vector2 pos, prevPos;
    protected Vector2 velocity = new Vector2();
    protected float speed;
    protected float timer, hitlagTimer;

    protected Box hurtbox;
    protected Attack attack = new Attack(){};
    protected int hp, maxHp;

    protected Entity(float x, float y) {
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
        hp -= attacker.getDamage();
        velocity.set(attacker.knockbackVector(getCenterPos()));
        setHitlagTimer(attacker.getHitlag());
        attacker.setHitlagTimer(hitlagTimer);
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
    public float getHitlag(){
        return attack.getHitlag();
    }

    @Override
    public void setHitlagTimer(float hitlag){
        if(hitlagTimer <= 0)
            hitlagTimer = hitlag;
    }

    @Override
    public Vector2 knockbackVector(Vector2 targetPos){
        return attack.knockbackVector(targetPos);
    }

    protected void placeHitboxes(){
        attack.placeHitboxes(velocity.cpy());
    }

    public Array<Circle> getHitboxes(){
        return new Array<>();
    }

    /**
     * Where we decide what this entity should do each frame.
     * @param deltaTime The time interval between each frame.
     */
    public void update(float deltaTime){
        timer -= deltaTime;
        prevPos.set(pos);
        if(animation != null)
            animation.update(deltaTime);
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

    public Array<ItemDrop> getItemDrops(){
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
        return animation == null? getPos() : getPos().add(animation.getOffset());
    }

    @Override
    public int getHp(){
        return hp;
    }

    @Override
    public int getMaxHp(){
        return maxHp;
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
