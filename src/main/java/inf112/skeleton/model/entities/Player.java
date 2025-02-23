package inf112.skeleton.model.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.controller.MyInputProcessor;

public class Player extends Entity implements ControllablePlayer{
    enum State {
        NonAttack, Attack
    }
    private State state = State.NonAttack;
    private boolean rightMove, leftMove, upMove, downMove;

    public Player(float x, float y){
        super(x, y);
        Gdx.input.setInputProcessor(new MyInputProcessor(this));
        this.hurtbox = new Rectangle(0, 0, 32, 32);
        this.speed = 140;
    }

    @Override
    public void update(float deltaTime){
        switch(state){
            case NonAttack -> updateNonAttack(deltaTime);
            case Attack -> updateAttack(deltaTime);
        }
    }

    // Can transition to: Attack
    private void updateNonAttack(float deltaTime){
        updateMotion();
        move(deltaTime);
        velocity.set(0,0);
    }

    // Can transition to: NonAttack
    private void updateAttack(float deltaTime){
    }

    private void updateMotion(){
        if (rightMove) velocity.x++;
        if (leftMove) velocity.x--;
        if(upMove) velocity.y++;
        if(downMove) velocity.y--;
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
}
