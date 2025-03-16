package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import inf112.skeleton.model.entities.Entity;

public class Attack {
    protected ObjectSet<Entity> hits = new ObjectSet<>();
    protected Array<Rectangle> hitBoxes = new Array<>();
    protected int damage;
    protected float range;
    protected float knockback;
    protected float momentum;
    protected float startup;
    protected float duration;
    protected float cooldown;
    protected float angle;

    public boolean addHit(Entity entity) {
        return hits.add(entity);
    }

    public void clearHits() {
        hits.clear();
    }

    public void placeHitboxes(Vector2 direction) {
        direction.setLength(range);

    }
}
