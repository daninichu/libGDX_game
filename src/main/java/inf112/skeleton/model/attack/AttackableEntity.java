package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.view.ViewableEntity;

public interface AttackableEntity extends ViewableEntity{
    void getAttacked(AttackableEntity attacker);

    void addHit(AttackableEntity target);

    boolean alreadyHit(AttackableEntity target);

    int getDamage();

    float getHitlag();

    void setHitlagTimer(float hitlagTimer);

    Iterable<Circle> getHitboxes();

    Vector2 knockbackVector(Vector2 targetPos);
}
