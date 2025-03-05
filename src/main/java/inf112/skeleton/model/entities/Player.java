package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.controller.ControllablePlayer;
import inf112.skeleton.model.entities.objects.GameObject;
import inf112.skeleton.view.UI;

public class Player extends Entity implements ControllablePlayer{
    enum State {
        NonAttack, Attack
    }
    private State state = State.NonAttack;
    private boolean rightMove, leftMove, upMove, downMove;

    public Array<GameObject> nearbyObjects = new Array<>();

    public Player(float x, float y){
        super(x, y);
        this.texture = new Texture("sprite16.png");
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.speed = 4.5f * MyGame.TILE_SIZE;
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

    @Override
    public boolean interact(UI ui){
        for(GameObject object : nearbyObjects){
            if(object.canInteract(this)){
                ui.setDialogue(object.dialogue());
                return true;
            }
        }
        return false;
    }
}
