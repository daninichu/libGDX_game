package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.controller.ControllablePlayer;

public class Player extends Entity implements ControllablePlayer{

    public Player(float x, float y){
        super(x, y);
        this.hurtbox = new Rectangle(0, 0, 32, 32);
        this.speed = 140;
    }

    @Override
    public void update(float deltaTime){
        move(deltaTime);
        velocity.set(0,0);
    }

    @Override
    public void moveRight(){
        velocity.x += speed;
    }

    @Override
    public void moveLeft(){
        velocity.x -= speed;
    }

    @Override
    public void moveUp(){
        velocity.y += speed;
    }

    @Override
    public void moveDown(){
        velocity.y -= speed;
    }
}
