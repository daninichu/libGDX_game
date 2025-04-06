package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.*;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.view.AnimationHandler;

public abstract class Enemy extends Entity{
    public enum State {
        Idle, Roaming, Chase, AttackStartup, Attack, AttackEnd, Stunned, Dying, Dead
    }
    public enum Event{
        Timeout, PlayerVisible, PlayerFar, PlayerClose
    }
    protected FsmBlueprint<State, Event> blueprint = new FsmBlueprint<>();
    protected StateMachine<State, Event> stateMachine = new StateMachine<>(blueprint, State.Idle);

    protected AttackableEntity player;
    private float timer;
    public static final float vision = MyGame.TILE_SIZE * 8;
    protected float attackRange;

    public Enemy(float x, float y, AttackableEntity player) {
        super(x, y);
        this.player = player;
        this.dir = Direction.RIGHT;

        addTransitions();
        addEnterFunctions();
        addExitFunctions();
    }

    protected void addTransitions(){
        blueprint.addTransition(State.Idle,             Event.Timeout,          State.Roaming);
        blueprint.addTransition(State.Idle,             Event.PlayerClose,      State.Chase);
        blueprint.addTransition(State.Idle,             Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Roaming,          Event.Timeout,          State.Idle);
        blueprint.addTransition(State.Roaming,          Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Chase,            Event.PlayerFar,        State.Idle);
        blueprint.addTransition(State.Chase,            Event.PlayerClose,      State.AttackStartup);
        blueprint.addTransition(State.AttackStartup,    Event.Timeout,          State.Attack);
        blueprint.addTransition(State.Attack,           Event.Timeout,          State.AttackEnd);
        blueprint.addTransition(State.AttackEnd,        Event.Timeout,          State.Chase);
        blueprint.addTransition(State.Stunned,          Event.Timeout,          State.Chase);
        blueprint.addTransition(State.Dying,            Event.Timeout,          State.Dead);
    }

    protected void addEnterFunctions(){
        stateMachine.onEnter(State.Idle, () -> {
            timer = MathUtils.random(1.2f, 3f);
            velocity.setLength(0);
            animation.setState(AnimationHandler.State.IDLE);
        });
        stateMachine.onEnter(State.Roaming, () -> {
            timer = MathUtils.random(0.2f, 2);
            velocity.setToRandomDirection();
            velocity.setLength(speed / 2f);
            animation.setState(AnimationHandler.State.RUN);
        });
        stateMachine.onEnter(State.Chase, () -> {
            velocity.set(speed, 0);
            animation.setState(AnimationHandler.State.RUN);
        });
        stateMachine.onEnter(State.AttackStartup, () -> {
            timer = attack.getStartup();
            velocity.set(player.getCenterPos().sub(getCenterPos()).setLength(0.1f));
            updateDirection();
            animation.setState(AnimationHandler.State.IDLE);
        });
        stateMachine.onEnter(State.Attack, () -> {
            placeHitboxes();
            timer = attack.getDuration();
            velocity.setLength(attack.getMomentum());
            animation.setState(AnimationHandler.State.RUN);
        });
        stateMachine.onEnter(State.AttackEnd, () -> {
            timer = attack.getCooldown();
            velocity.set(0, 0);
            animation.setState(AnimationHandler.State.IDLE);
        });
        stateMachine.onEnter(State.Stunned, () -> {
            timer = 0.35f;
            animation.setState(AnimationHandler.State.HIT);
        });
        stateMachine.onEnter(State.Dying, () -> {
            timer = 1f;
            animation.setState(AnimationHandler.State.DEATH);
        });
    }

    protected void addExitFunctions(){
        stateMachine.onExit(State.Attack, () -> attack.reset());
        stateMachine.onExit(State.AttackEnd, () -> animation.setState(AnimationHandler.State.IDLE));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float distance = getCenterPos().dst(player.getCenterPos());
        if (distance <= attackRange)
            stateMachine.fireEvent(Event.PlayerClose);
        else if (distance <= vision)
            stateMachine.fireEvent(Event.PlayerVisible);
        else if (distance > vision)
            stateMachine.fireEvent(Event.PlayerFar);

        switch(stateMachine.getState()){
            case Idle, Roaming -> updateDirection();
            case Chase -> {
                float angle = player.getCenterPos().sub(getCenterPos()).angleRad();
                velocity.setAngleRad(angle);
                updateDirection();
            }
            case Stunned, Dying -> velocity.scl((float) Math.pow(0.01f, deltaTime));
        }

        move(deltaTime);

        getAttacked(player);
        player.getAttacked(this);

        timer -= deltaTime;
        if (timer <= 0)
            stateMachine.fireEvent(Event.Timeout);
        if(stateMachine.getState() != State.Dying && stateMachine.getState() != State.Dead && health <= 0)
            stateMachine.forceState(State.Dying);
    }

    @Override
    public void getAttacked(AttackableEntity attacker) {
        if(gotHit(attacker) && stateMachine.getState() != State.Dying){
            super.getAttacked(attacker);
            stateMachine.forceState(State.Stunned);

            dir = Direction.leftOrRight(velocity).opposite();
            animation.setDirection(dir);
        }
    }

    @Override
    protected void updateDirection(){
        dir = Direction.leftOrRight(velocity);
        animation.setDirection(dir);
    }

    protected void placeHitboxes(){}

    public State getState() {
        return stateMachine.getState();
    }

    @Override
    public boolean dead(){
        return stateMachine.getState() == State.Dead;
    }
}
