package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.enemies.Enemy;

public abstract class Attack {
    protected ObjectSet<AttackableEntity> hits = new ObjectSet<>();
    protected Array<Circle> hitboxes = new Array<>();
    protected int damage;
    protected float range;
    protected float knockback;
    protected float momentum;
    protected float startup;
    protected float duration;
    protected float cooldown;
    protected float angle;

    public boolean addHit(AttackableEntity target) {
        return hits.add(target);
    }

    public boolean alreadyHit(AttackableEntity target){
        return hits.contains(target);
    }

    public void reset() {
        hits.clear();
        hitboxes.clear();
    }

    public abstract void placeHitboxes(Vector2 direction);

    public Array.ArrayIterable<Circle> getHitboxes() {
        return new Array.ArrayIterable<>(hitboxes);
    }

    public Vector2 knockbackVector() {
        return new Vector2(1, 0).setAngleDeg(angle).setLength(knockback);
    }

    public int getDamage() {
        return damage;
    }

    public float getRange(){
        return range;
    }

    public float getKnockback(){
        return knockback;
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

    public float getAngle(){
        return angle;
    }
}
