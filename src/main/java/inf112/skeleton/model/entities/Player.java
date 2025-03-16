package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.collision.StaticCollisionHandler;
import inf112.skeleton.model.entities.gameObjects.GameObject;

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

    private PlayerAttack attack = new PlayerAttack();
    private float timer;
    private float invincibleTimer;
    private boolean rightMove, leftMove, upMove, downMove;

    public Player(float x, float y){
        super(x, y);
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.health = 20;
        this.mass = 1;
        this.speed = 4.5f * MyGame.TILE_SIZE;

        addEnterFunctions();
        addExitFunctions();
    }

    private void addEnterFunctions(){
        stateMachine.onEnter(State.AttackStartup, () -> {
            timer = 0.2f;
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
            timer = 0.15f;
            velocity.setLength(speed / 2);
            placeHitboxes();
        });
        stateMachine.onEnter(State.AttackEnd, () -> timer = 0.2f);
        stateMachine.onEnter(State.Stunned, () -> timer = 0.5f);
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
            case Stunned -> move(deltaTime);
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

    // Can transition to: NonAttack
    private void updateAttack(float deltaTime){
        move(deltaTime);
    }

    private void updateMotion(){
        if(rightMove)   velocity.x++;
        if(leftMove)    velocity.x--;
        if(upMove)      velocity.y++;
        if(downMove)    velocity.y--;
    }

    void placeHitboxes(){
        attack.placeHitboxes(velocity.cpy());
    }

    @Override
    public Array<Rectangle> getHitboxes(){
        Array<Rectangle> result = new Array<>();
        for(Rectangle hitbox : attack.getHitboxes()){
            Rectangle adjustedHitbox = new Rectangle(hitbox);
            adjustedHitbox.setPosition(hitbox.x + getCenterX(), hitbox.y + getCenterY());
            result.add(adjustedHitbox);
        }
        return result;
    }

    @Override
    public Attack getAttack(){
        return attack;
    }

//    @Override
//    public boolean alreadyHit(AttackableEntity target){
//        return attack.alreadyHit(target);
//    }

    @Override
    public void getAttacked(AttackableEntity attacker){
        if(invincibleTimer > 0){
            return;
        }
        if(StaticCollisionHandler.collidesAny(this, attacker.getHitboxes())){
            health -= attacker.getAttack().getDamage();
            stateMachine.forceState(State.Stunned);
            velocity.set(attacker.getAttack().knockbackVector());
            invincibleTimer = 1.8f;
            System.out.println("Damage taken. Health: " + health);
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
        private Rectangle baseHitBox = new Rectangle(0, 0, MyGame.TILE_SIZE*2, MyGame.TILE_SIZE*2);

        private PlayerAttack(){
            this.damage = 3;
            this.knockback = MyGame.TILE_SIZE*8;
            this.startup = 0.2f;
            this.duration = 0.15f;
            this.cooldown = 0.2f;
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            angle = direction.angleDeg();
            Rectangle hitbox = new Rectangle(baseHitBox);
            hitbox.setCenter(direction.setLength(MyGame.TILE_SIZE));
            hitboxes.add(hitbox);
        }

        public void placeHitboxes(Array<Rectangle> hitboxes){
            this.hitboxes.addAll(hitboxes);
        }
    }
}
