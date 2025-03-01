package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;

import java.util.HashMap;

public class Player extends Entity implements ControllablePlayer{
    enum State {
        NonAttack, Attack
    }
    private State state = State.NonAttack;
    private boolean rightMove, leftMove, upMove, downMove;

    public Player(float x, float y){
        super(x, y);
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.speed = 4.5f * MyGame.TILE_SIZE;
        HashMap<String,String> map = new HashMap<>();
    }

    @Override
    public void update(float deltaTime){
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
