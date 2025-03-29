package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.model.Direction;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.model.inventory.HealthPotion;
import inf112.skeleton.model.inventory.IInventoryPlayer;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.animation.EntityAnimation;
import inf112.skeleton.view.animation.PlayerAnimation;

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

    private float timer;
    private float invincibleTimer;
    private boolean rightMove, leftMove, upMove, downMove;
    private Inventory inventory = new Inventory();

    public Player(float x, float y){
        super(x, y);
        this.animation = new PlayerAnimation();

        this.speed = 5f * MyGame.TILE_SIZE;
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.hurtbox = new Box(2, 0, 12, 12);
        this.attack = new PlayerAttack();
        this.maxHealth = this.health = 20;
//        this.mass = 10;

        addEnterFunctions();
        addExitFunctions();

        inventory.addItem(new HealthPotion());
    }

    private void addEnterFunctions(){
        stateMachine.onEnter(State.NonAttack, () -> {
            animation.setState(EntityAnimation.State.IDLE);
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
            animation.setState(EntityAnimation.State.ATTACK);
            animation.setFrameDuration(attack.getStartup());
        });
        stateMachine.onEnter(State.Attack, () -> {
            timer = attack.getDuration();
            velocity.setLength(attack.getMomentum());
            placeHitboxes();
            animation.setFrameDuration(attack.getDuration()/3);
        });
        stateMachine.onEnter(State.AttackEnd, () -> {
            timer = attack.getCooldown();
            animation.setFrameDuration(attack.getCooldown()/2);
        });
        stateMachine.onEnter(State.Stunned, () -> {
            timer = 0.75f;
            animation.setState(EntityAnimation.State.HIT);
        });
    }

    private void addExitFunctions(){
        stateMachine.onExit(State.Attack, () -> {
            attack.reset();
        });
        stateMachine.onExit(State.AttackEnd, () -> {
            animation.setState(EntityAnimation.State.IDLE);
            animation.setDirection(dir, velocity);
        });
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        switch (stateMachine.getState()){
            case NonAttack -> {
                velocity.set(0,0);
                updateMotion();
                velocity.setLength(speed);
                if(move(deltaTime)){
                    updateDirection();
                    animation.setState(EntityAnimation.State.RUN);
                }
                else
                    animation.setState(EntityAnimation.State.IDLE);
            }
            case Attack -> move(deltaTime);
            case Stunned -> {
                move(deltaTime);
                velocity.scl((float) Math.pow(0.1f, deltaTime));
            }
        }

        invincibleTimer -= deltaTime;
        timer -= deltaTime;
        if(timer <= 0)
            stateMachine.fireEvent(Event.Timeout);
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
        if(invincibleTimer <= 0 && gotHit(attacker)){
            super.getAttacked(attacker);
            stateMachine.forceState(State.Stunned);
            dir = Direction.fromVector(velocity).opposite();
            animation.setDirection(dir, velocity.cpy().scl(-1));
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
        health = Math.min(maxHealth, health + item.getHeal());
        return true;
    }

    @Override
    public GameObject interact(Array.ArrayIterable<GameObject> objects){
        for(GameObject object : objects)
            if(object.inInteractionRange())
                return object;
        return null;
    }

    private class PlayerAttack extends Attack{
        private PlayerAttack(){
            this.damage = 3;
            this.momentum = speed * 0.5f;
            this.knockback = MyGame.TILE_SIZE*8;
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
