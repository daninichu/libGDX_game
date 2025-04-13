package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.ai.FsmBlueprint;
import inf112.skeleton.model.ai.PathFinder;
import inf112.skeleton.model.ai.StateMachine;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.collision.CollisionHandler;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.util.Direction;
import inf112.skeleton.util.Line;
import inf112.skeleton.view.AnimationHandler;

import java.awt.*;

public abstract class Enemy extends Entity{
    public enum State {
        Idle, Roaming, Chase, AttackStartup, Attack, AttackEnd, Stunned, Dying, Dead
    }
    public enum Event{
        Timeout, PlayerClose, PlayerVisible, PlayerNotVisible, PlayerFar
    }
    protected FsmBlueprint<State, Event> blueprint = new FsmBlueprint<>();
    protected StateMachine<State, Event> stateMachine = new StateMachine<>(blueprint, State.Idle);

    protected HashGrid<Rectangle> grid;
    protected PathFinder pathFinder;
    protected Queue<Point> path;
    protected Line ray;
    protected AttackableEntity player;
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

    public void setPathFinder(HashGrid<Rectangle> grid, PathFinder pathFinder) {
        this.grid = grid;
        this.pathFinder = pathFinder;
    }

    protected void addTransitions(){
        blueprint.addTransition(State.Idle,             Event.Timeout,          State.Roaming);
        blueprint.addTransition(State.Idle,             Event.PlayerClose,      State.Chase);
        blueprint.addTransition(State.Idle,             Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Roaming,          Event.Timeout,          State.Idle);
        blueprint.addTransition(State.Roaming,          Event.PlayerVisible,    State.Chase);
        blueprint.addTransition(State.Chase,            Event.PlayerFar,        State.Idle);
        blueprint.addTransition(State.Chase,            Event.PlayerClose,      State.AttackStartup);
        blueprint.addTransition(State.Chase,            Event.PlayerNotVisible, State.Idle);
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
            animation.setState(AnimationHandler.State.ATTACK);
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
        if(hitlagTimer > 0){
            hitlagTimer -= deltaTime;
            return;
        }
        super.update(deltaTime);
        updateState();
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
    }

    private void updateState(){
        float distance = getCenterPos().dst(player.getCenterPos());
        if (distance <= vision){
            if(playerVisible())
                stateMachine.fireEvent(distance <= attackRange? Event.PlayerClose : Event.PlayerVisible);
            else
                stateMachine.fireEvent(Event.PlayerNotVisible);
        }
        else{
            ray = null;
            stateMachine.fireEvent(Event.PlayerFar);
        }
        if (timer <= 0)
            stateMachine.fireEvent(Event.Timeout);
    }

    private boolean playerVisible(){
        ray = new Line(getCenterPos(), player.getCenterPos());
        return !CollisionHandler.collidesAny(ray, grid.getLocalObjects(ray));
    }

    private void followPath(){
        path = pathFinder.findPath(getCenterPos(), player.getCenterPos());
    }

    @Override
    public void getAttacked(AttackableEntity attacker) {
        if(gotHit(attacker) && getState() != State.Dying && getState() != State.Dead){
            super.getAttacked(attacker);
            stateMachine.forceState(health > 0 ? State.Stunned : State.Dying);

            dir = Direction.leftOrRight(velocity).opposite();
            animation.setDirection(dir);
        }
    }

    @Override
    protected void updateDirection(){
        dir = Direction.leftOrRight(velocity);
        animation.setDirection(dir);
    }

    @Override
    public Array<Circle> getHitboxes(){
        Array<Circle> result = new Array<>();
        for(Circle hitbox : attack.getHitboxes()){
            Circle adjustedHitbox = new Circle(hitbox);
            adjustedHitbox.setPosition(getCenterPos());
            result.add(adjustedHitbox);
        }
        return result;
    }

    public Line getRay() {
        return ray;
    }

    public State getState() {
        return stateMachine.getState();
    }

    @Override
    public boolean dead(){
        return stateMachine.getState() == State.Dead;
    }
}
