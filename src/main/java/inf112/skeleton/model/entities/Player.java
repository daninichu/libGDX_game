package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.util.Direction;
import inf112.skeleton.model.ai.FsmBlueprint;
import inf112.skeleton.model.ai.StateMachine;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.inventory.IInventoryPlayer;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.AnimationHandler;

public class Player extends Entity implements ControllablePlayer, IInventoryPlayer{
    public enum State{
        NonAttack, AttackStartup, Attack, AttackEnd, Stunned
    }
    public enum Event{
        Timeout, AttackPressed
    }
    private static final FsmBlueprint<State, Event> blueprint = new FsmBlueprint<>();
    static {
        blueprint.addTransition(State.NonAttack,        Event.AttackPressed,     State.AttackStartup);
        blueprint.addTransition(State.AttackStartup,    Event.Timeout,           State.Attack);
        blueprint.addTransition(State.Attack,        Event.Timeout,           State.AttackEnd);
        blueprint.addTransition(State.AttackEnd,        Event.Timeout,           State.NonAttack);
        blueprint.addTransition(State.AttackEnd,        Event.AttackPressed,     State.AttackStartup);
        blueprint.addTransition(State.Stunned,          Event.Timeout,           State.NonAttack);
    }
    private final StateMachine<State, Event> stateMachine = new StateMachine<>(blueprint, State.NonAttack);

    private float invincibleTimer;
    private boolean rightMove, leftMove, upMove, downMove;
    private Inventory inventory = new Inventory();

    public Player(float x, float y){
        super(x, y);
        this.animation = new AnimationHandler("char", dir){
            @Override
            protected void putOffsets(){
                ObjectMap<Direction, Vector2> attackOffsets = new ObjectMap<>();
                attackOffsets.put(Direction.LEFT, new Vector2(-16, -16));
                attackOffsets.put(Direction.RIGHT, new Vector2(0, -16));
                attackOffsets.put(Direction.UP, new Vector2(-16, 0));
                attackOffsets.put(Direction.DOWN, new Vector2(-16, -16));
                offsets.put(State.ATTACK, attackOffsets);
            }
        };

        this.speed = 5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(2, 0, 12, 12);
        this.attack = new PlayerAttack();
        this.maxHealth = this.health = 20;
//        this.mass = 10;

        addEnterFunctions();
        addExitFunctions();
    }

    private void addEnterFunctions(){
        stateMachine.onEnter(State.NonAttack, () -> {
            animation.setState(AnimationHandler.State.IDLE);
        });
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
            animation.setState(AnimationHandler.State.ATTACK);
            animation.setFrameDuration(attack.getStartup(), AnimationHandler.State.ATTACK);
        });
        stateMachine.onEnter(State.Attack, () -> {
            timer = attack.getDuration();
            velocity.setLength(attack.getMomentum());
            placeHitboxes();
            animation.setFrameDuration(attack.getDuration()/3, AnimationHandler.State.ATTACK);
        });
        stateMachine.onEnter(State.AttackEnd, () -> {
            timer = attack.getCooldown();
            animation.setFrameDuration(attack.getCooldown()/2, AnimationHandler.State.ATTACK);
        });
        stateMachine.onEnter(State.Stunned, () -> {
            timer = 0.75f;
            animation.setState(AnimationHandler.State.HIT);
        });
    }

    private void addExitFunctions(){
        stateMachine.onExit(State.Attack, () -> {
            attack.reset();
        });
        stateMachine.onExit(State.AttackEnd, () -> {
            animation.setState(AnimationHandler.State.IDLE);
            animation.setDirection(dir);
        });
    }

    @Override
    public void update(float deltaTime){
        if(hitlagTimer > 0){
            hitlagTimer -= deltaTime;
            return;
        }
        super.update(deltaTime);
        switch (stateMachine.getState()){
            case NonAttack -> {
                velocity.set(0,0);
                updateMotion();
                velocity.setLength(speed);
                if(move(deltaTime)){
                    updateDirection();
                    animation.setState(AnimationHandler.State.RUN);
                }
                else
                    animation.setState(AnimationHandler.State.IDLE);
            }
            case Attack -> move(deltaTime);
            case Stunned -> {
                move(deltaTime);
                velocity.scl((float) Math.pow(0.1f, deltaTime));
            }
        }

        invincibleTimer -= deltaTime;
//        timer -= deltaTime;
        if(timer <= 0)
            stateMachine.fireEvent(Event.Timeout);
    }

    private void updateMotion(){
        if(rightMove)   velocity.x++;
        if(leftMove)    velocity.x--;
        if(upMove)      velocity.y++;
        if(downMove)    velocity.y--;
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
        if(invincibleTimer <= 0 && gotHit(attacker)){
            super.getAttacked(attacker);
            stateMachine.forceState(State.Stunned);
            dir = Direction.fromVector(velocity).opposite();
            animation.setDirection(dir);
            invincibleTimer = 1.8f;
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
    public void attackPressed(){
        stateMachine.fireEvent(Event.AttackPressed);
    }

    @Override
    public Inventory getInventory(){
        return inventory;
    }

    @Override
    public boolean useItem(Item item){
        if(item == null)
            return false;
        health = Math.min(maxHealth, health + item.heal());
        speed *= item.speedMultiplier();
        return true;
    }

    private class PlayerAttack extends Attack{
        private PlayerAttack(){
            this.damage = 7;
            this.momentum = speed * 0.5f;
            this.knockback = MyGame.TILE_SIZE * 20f;
            this.hitlag = 0.5f;
            this.startup = 0.35f;
            this.duration = 0.25f;
            this.cooldown = 0.35f;


            this.damage = 3;
            this.momentum = speed * 0.5f;
            this.knockback = MyGame.TILE_SIZE * 8;
            this.hitlag = 0.125f;
            this.startup = 0.2f;
            this.duration = 0.15f;
            this.cooldown = 0.2f;

        }

        @Override
        public void placeHitboxes(Vector2 direction){
            this.direction = direction;
            Circle hitbox = new Circle(0, 0, MyGame.TILE_SIZE * 0.75f);
            hitbox.setPosition(direction.setLength(MyGame.TILE_SIZE * 0.5f));
            hitboxes.add(hitbox);
        }
    }
}
