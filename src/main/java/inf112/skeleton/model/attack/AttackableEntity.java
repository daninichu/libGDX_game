package inf112.skeleton.model.attack;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.view.ViewableEntity;

public interface AttackableEntity extends ViewableEntity{
    void getAttacked(int damage);

    Iterable<Rectangle> getHitboxes();
}
