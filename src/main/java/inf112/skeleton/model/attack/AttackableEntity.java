package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.view.ViewableEntity;

public interface AttackableEntity extends ViewableEntity{
    Attack getAttack();

    void getAttacked(AttackableEntity attacker);

//    boolean alreadyHit(AttackableEntity target);

    Iterable<Circle> getHitboxes();

}
