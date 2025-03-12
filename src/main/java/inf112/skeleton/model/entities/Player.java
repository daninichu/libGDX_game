package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.view.UI;

public class Player extends Entity implements ControllablePlayer{
    private static FsmBlueprint blueprint = new FsmBlueprint();
    static {
        blueprint.addTransition("nonAttack", "attackPressed", "attackWindUp");
        blueprint.addTransition("attackWindUp", "timeout", "attacking");
        blueprint.addTransition("attacking", "timeout", "nonAttack");
    }
    private StateMachine stateMachine = new StateMachine(blueprint, "nonAttack");
    enum State {
        NonAttack, Attack
    }
    private State state = State.NonAttack;
    private float invincibleTimer;
    private boolean rightMove, leftMove, upMove, downMove;

    public Player(float x, float y){
        super(x, y);
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.health = 20;
        this.speed = 4.5f * MyGame.TILE_SIZE;
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        invincibleTimer -= deltaTime;
        switch (state){
            case NonAttack -> updateNonAttack(deltaTime);
            case Attack -> updateAttack(deltaTime);
        }
    }

    // Can transition to: Attack
    private void updateNonAttack(float deltaTime){
        velocity.set(0,0);
        updateMotion();
        velocity.setLength(speed);
        prevPos.set(pos);
        move(deltaTime);
    }

    // Can transition to: NonAttack
    private void updateAttack(float deltaTime){
        // TODO
    }

    private void updateMotion(){
        if(rightMove)velocity.x++;
        if(leftMove) velocity.x--;
        if(upMove)   velocity.y++;
        if(downMove) velocity.y--;
    }

    @Override
    public void takeDamage(int damage){
        if(invincibleTimer <= 0){
            health -= damage;
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
    public GameObject interact(Array.ArrayIterable<GameObject> objects){
        for(GameObject object : objects)
            if(object.canInteract())
                return object;
        return null;
    }
}
