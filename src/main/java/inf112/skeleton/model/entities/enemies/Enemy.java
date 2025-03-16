package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.*;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.entities.Entity;

public abstract class Enemy extends Entity{
    public enum State {
        Idle, Roaming, Chase, AttackStartup, Attacking, AttackEnd
    }
    public enum Event{
        Timeout, PlayerVisible, PlayerFar, PlayerClose
    }
    protected FsmBlueprint<State, Event> blueprint = new FsmBlueprint<>();
    private StateMachine<State, Event> stateMachine = new StateMachine<>(blueprint, State.Idle);

    private AttackableEntity player;
    private float timer;
    public static final float vision = MyGame.TILE_SIZE * 8;
    protected float attackRange;

    public Enemy(float x, float y, AttackableEntity player) {
        super(x, y);
        this.player = player;

        addTransitions();
        addEnterFunctions();
        addExitFunctions();
    }

    protected void addTransitions(){
        blueprint.addTransition(State.Idle,             Event.Timeout,          State.Roaming);
        blueprint.addTransition(State.Idle,             Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Roaming,          Event.Timeout,          State.Idle);
        blueprint.addTransition(State.Roaming,          Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Chase,            Event.PlayerFar,        State.Idle);
        blueprint.addTransition(State.Chase,            Event.PlayerClose,      State.AttackStartup);
        blueprint.addTransition(State.AttackStartup,    Event.Timeout,          State.Attacking);
        blueprint.addTransition(State.Attacking,        Event.Timeout,          State.AttackEnd);
        blueprint.addTransition(State.AttackEnd,        Event.Timeout,          State.Chase);
    }

    protected void addEnterFunctions(){
        stateMachine.onEnter(State.Idle, () -> {
            timer = MathUtils.random(1.2f, 3f);
            velocity.setLength(0);
        });
        stateMachine.onEnter(State.Roaming, () -> {
            timer = MathUtils.random(0.2f, 2);
            velocity.setToRandomDirection();
            velocity.setLength(speed / 2f);
        });
        stateMachine.onEnter(State.Chase, () -> {
            velocity.set(speed, 0);
        });
        stateMachine.onEnter(State.AttackStartup, () -> {
            timer = 0.3f;
            velocity.set(player.getCenterPos().sub(getCenterPos()).setLength(0.1f));
        });
        stateMachine.onEnter(State.Attacking, () -> {
            placeHitboxes();
            timer = 0.4f;
            velocity.setLength(speed*3);
        });
        stateMachine.onEnter(State.AttackEnd, () -> {
            timer = 0.8f;
            velocity.set(0, 0);
        });
    }

    protected void addExitFunctions(){
        stateMachine.onExit(State.Attacking, () -> hitboxes.clear());
    }

    @Override
    public void update(float deltaTime) {
        float distance = getCenterPos().dst(player.getCenterPos());
        if (distance <= attackRange)
            stateMachine.fireEvent(Event.PlayerClose);
        else if (distance <= vision)
            stateMachine.fireEvent(Event.PlayerVisible);
        else if (distance > vision)
            stateMachine.fireEvent(Event.PlayerFar);

        if (stateMachine.getState().equals(State.Chase)) {
            float angle = player.getCenterPos().sub(getCenterPos()).angleRad();
            velocity.setAngleRad(angle);
        }

        prevPos.set(pos);
        move(deltaTime);
        attack(player);
        getAttacked();

        timer -= deltaTime;
        if (timer <= 0) {
            stateMachine.fireEvent(Event.Timeout);
        }
    }

    private void getAttacked() {
        for(Rectangle hitbox : player.getHitboxes()) {
            if(locateHurtbox().overlaps(hitbox)) {
                getAttacked(1);
                stateMachine.forceState(State.Idle);
                break;
            }
        }
    }

    protected void placeHitboxes(){}

    public State getState() {
        return stateMachine.getState();
    }

    public float getAttackRange(){
        return attackRange;
    }
}
