package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public abstract class Attack {
    protected ObjectSet<AttackableEntity> hits = new ObjectSet<>();
    protected Array<Circle> hitboxes = new Array<>();
    protected Vector2 direction = new Vector2(1, 0);
    protected int damage;
    protected float knockback;
    protected float hitStun;
    protected float momentum;
    protected float startup;
    protected float duration;
    protected float cooldown;

    public void addHit(AttackableEntity target) {
        hits.add(target);
    }

    public boolean alreadyHit(AttackableEntity target){
        return hits.contains(target);
    }

    public void reset() {
        hits.clear();
        hitboxes.clear();
    }

    public void placeHitboxes(Vector2 direction){}

    public Array.ArrayIterable<Circle> getHitboxes() {
        return new Array.ArrayIterable<>(hitboxes);
    }

    public Vector2 knockbackVector(Vector2 targetPos) {
        return direction.setLength(knockback);
    }

    public int getDamage() {
        return damage;
    }

    public float getHitStun() {
        return hitStun;
    }

    public float getMomentum(){
        return momentum;
    }

    public float getStartup(){
        return startup;
    }

    public float getDuration(){
        return duration;
    }

    public float getCooldown(){
        return cooldown;
    }
}
