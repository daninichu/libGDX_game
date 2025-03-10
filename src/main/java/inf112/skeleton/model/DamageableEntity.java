package inf112.skeleton.model;

import inf112.skeleton.view.ViewableEntity;

public interface DamageableEntity extends ViewableEntity{
    void takeDamage(int damage);
}
