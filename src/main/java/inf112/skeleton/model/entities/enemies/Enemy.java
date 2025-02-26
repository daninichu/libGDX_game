package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.states.enemies.EnemyState;
import inf112.skeleton.model.states.enemies.PatrolState;
import inf112.skeleton.view.ViewableEntity;

public abstract class Enemy extends Entity {
    private EnemyState currentState;

    protected Vector2 target = new Vector2();
    public final float attackRange;

    public Enemy(float x, float y, float attackRange, ViewableEntity player) {
        super(x, y);
        this.currentState = new PatrolState(this, player);
        this.attackRange = attackRange;
    }

    @Override
    public void update(float deltaTime){
        currentState.update(deltaTime);
    }

    public void changeState(EnemyState newState) {
        currentState.exit();
        currentState = newState;
        currentState.enter();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setTarget(Vector2 newTarget) {
        target.set(newTarget);
    }

    public boolean moveEnemy(float deltaTime){
        return move(deltaTime);
    }

    public String currentState(){
        return currentState.toString();
    }
}
