package inf112.skeleton.model.states.enemies;

import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.view.ViewableEntity;

public abstract class EnemyState {
    /** For modification of variables.*/
    protected Enemy enemy;

    /** To know where and how far the player is.*/
    protected ViewableEntity player;

    public EnemyState(Enemy enemy, ViewableEntity player) {
        this.enemy = enemy;
        this.player = player;
    }

    public void enter(){}

    public abstract void update(float deltaTime);

    public void exit(){}
}
