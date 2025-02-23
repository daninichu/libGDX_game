package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.view.ViewableEntity;

public abstract class Enemy extends Entity{
    protected enum State {
        Patrol, Search, Pursue, Attack
    }
    protected ViewableEntity player;
    protected State state = State.Patrol;
    protected Vector2 target = new Vector2();
    public static final float vision = 32*8;
    public static final float attackRange = 32*2;

    public Enemy(float x, float y, ViewableEntity player) {
        super(x, y);
        this.player = player;
    }

    @Override
    public void update(float deltaTime){
        Gdx.app.log("Enemy State", ""+state);
        switch(state){
            case Patrol -> updatePatrol(deltaTime);
            case Search -> updateSearch(deltaTime);
            case Pursue -> updatePursue(deltaTime);
            case Attack -> updateAttack(deltaTime);
        }
    }

    // Can transition to: Pursue
    private void updatePatrol(float deltaTime){
        Vector2 playerCenter = new Vector2(player.getCenterX(), player.getCenterY());
        if(playerCenter.dst(getCenterX(), getCenterY()) <= Enemy.vision){
            target.set(playerCenter);
            state = State.Pursue;
        }
    }

    // Can transition to: Patrol, Pursue
    private void updateSearch(float deltaTime){
        Vector2 trueTarget = new Vector2(player.getCenterX(), player.getCenterY());
        if(trueTarget.dst(getCenterX(), getCenterY()) <= vision){
            target.set(trueTarget);
            state = State.Pursue;
            return;
        }
        Vector2 toTarget = target.cpy().sub(getCenterX(), getCenterY());
        velocity.add(toTarget);
        move(deltaTime);
        velocity.set(0, 0);
        // Reached last known player position. Player isn't here. Go back to Patrolling.
        if(target.dst(getCenterX(), getCenterY()) < 8){
            state = State.Patrol;
        }
    }

    // Can transition to: Search, Attack
    private void updatePursue(float deltaTime){
        Vector2 trueTarget = new Vector2(player.getCenterX(), player.getCenterY());
        if(trueTarget.dst(getCenterX(), getCenterY()) > vision){
            state = State.Search;
            return;
        }
        target.set(trueTarget);
        Vector2 toTarget = target.cpy().sub(getCenterX(), getCenterY());
        velocity.add(toTarget);
        move(deltaTime);
        velocity.set(0, 0);

        if(target.dst(getCenterX(), getCenterY()) <= attackRange){
            state = State.Pursue;
        }
    }

    // Can transition to: Pursue
    private void updateAttack(float deltaTime){

    }

    public void setTarget(Vector2 target){
        this.target = target;
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
    }
}
