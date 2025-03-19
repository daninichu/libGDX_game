package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.util.Box;

public class Player extends Entity implements ControllablePlayer{
    public enum State{
        NonAttack, AttackStartup, Attacking, AttackEnd, Stunned
    }
    public enum Event{
        Timeout, AttackPressed
    }
    private static final FsmBlueprint<State, Event> blueprint = new FsmBlueprint<>();
    static {
        blueprint.addTransition(State.NonAttack,        Event.AttackPressed,     State.AttackStartup);
        blueprint.addTransition(State.AttackStartup,    Event.Timeout,           State.Attacking);
        blueprint.addTransition(State.Attacking,        Event.Timeout,           State.AttackEnd);
        blueprint.addTransition(State.AttackEnd,        Event.Timeout,           State.NonAttack);
        blueprint.addTransition(State.AttackEnd,        Event.AttackPressed,     State.AttackStartup);
        blueprint.addTransition(State.Stunned,          Event.Timeout,           State.NonAttack);
    }
    private final StateMachine<State, Event> stateMachine = new StateMachine<>(blueprint, State.NonAttack);

    private float timer;
    private float invincibleTimer;
    private boolean rightMove, leftMove, upMove, downMove;

    public Player(float x, float y){
        super(x, y);
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attack = new PlayerAttack();
        this.health = 20;
        this.mass = 1;
        this.speed = 4.5f * MyGame.TILE_SIZE;

        addEnterFunctions();
        addExitFunctions();
    }

    private void addEnterFunctions(){
        stateMachine.onEnter(State.AttackStartup, () -> {
            timer = attack.getStartup();
            if(velocity.isZero()){
                switch(dir){
                    case LEFT   -> velocity.set(-1, 0);
                    case RIGHT  -> velocity.set(1, 0);
                    case UP     -> velocity.set(0, 1);
                    case DOWN   -> velocity.set(0, -1);
                }
            }
        });
        stateMachine.onEnter(State.Attacking, () -> {
            timer = attack.getDuration();
            velocity.setLength(attack.getMomentum());
            placeHitboxes();
        });
        stateMachine.onEnter(State.AttackEnd, () -> {
            timer = attack.getCooldown();
        });
        stateMachine.onEnter(State.Stunned, () -> {
            timer = 0.5f;
        });
    }

    private void addExitFunctions(){
        stateMachine.onExit(State.Attacking, () -> attack.reset());
    }

    @Override
    public void update(float deltaTime){
        prevPos.set(pos);
        switch (stateMachine.getState()){
            case NonAttack -> updateNonAttack(deltaTime);
            case Attacking -> move(deltaTime);
            case Stunned -> {
                move(deltaTime);
                velocity.scl(0.98f);
            }
        }

        invincibleTimer -= deltaTime;
        timer -= deltaTime;
        if(timer <= 0){
            stateMachine.fireEvent(Event.Timeout);
        }
    }

    // Can transition to: Attack
    private void updateNonAttack(float deltaTime){
        velocity.set(0,0);
        updateMotion();
        velocity.setLength(speed);
        move(deltaTime);
    }

    private void updateMotion(){
        if(rightMove)   velocity.x++;
        if(leftMove)    velocity.x--;
        if(upMove)      velocity.y++;
        if(downMove)    velocity.y--;
    }

    private void placeHitboxes(){
        attack.placeHitboxes(velocity.cpy());
    }

    @Override
    public Array<Circle> getHitboxes(){
        Array<Circle> result = new Array<>();
        for(Circle hitbox : attack.getHitboxes()){
            Circle adjustedHitbox = new Circle(hitbox);
            adjustedHitbox.setPosition(hitbox.x + getCenterX(), hitbox.y + getCenterY());
            result.add(adjustedHitbox);
        }
        return result;
    }

    @Override
    public void getAttacked(AttackableEntity attacker){
        if(invincibleTimer > 0 || attacker.alreadyHit(this)){
            return;
        }
        for(Circle hitbox : attacker.getHitboxes()){
            if(locateHurtbox().overlaps(hitbox)){
                health -= attacker.getAttack().getDamage();
                stateMachine.forceState(State.Stunned);
                velocity.set(attacker.getAttack().knockbackVector(getCenterPos()));
                invincibleTimer = 1.8f;
            }
        }
    }

    @Override
    public void setRightMove(boolean t){
        rightMove = t;
    }
    @Override
    public void setLeftMove(boolean t){
        leftMove = t;
    }
    @Override
    public void setUpMove(boolean t){
        upMove = t;
    }
    @Override
    public void setDownMove(boolean t){
        downMove = t;
    }

    @Override
    public void attack(){
        stateMachine.fireEvent(Event.AttackPressed);
    }

    @Override
    public GameObject interact(Array.ArrayIterable<GameObject> objects){
        for(GameObject object : objects)
            if(object.inInteractionRange())
                return object;
        return null;
    }

    private static class PlayerAttack extends Attack{
        private Circle baseHitBox = new Circle(0, 0, MyGame.TILE_SIZE);

        private PlayerAttack(){
            this.damage = 3;
            this.momentum = 2.5f * MyGame.TILE_SIZE;
            this.knockback = MyGame.TILE_SIZE*8;
            this.startup = 0.2f;
            this.duration = 0.15f;
            this.cooldown = 0.2f;
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            angle = direction.angleDeg();
            Circle hitbox = new Circle(baseHitBox);
            hitbox.setPosition(direction.setLength(MyGame.TILE_SIZE));
            hitboxes.add(hitbox);
        }
    }
}
