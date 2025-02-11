package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.controller.ControllablePlayer;

public class Player extends Entity implements ControllablePlayer{

    public Player(float x, float y){
        super(x, y);
        this.hitbox = new Rectangle(0, 0, 32, 32);
        this.speed = 128;
    }

    @Override
    public void update(float deltaTime){
        move(deltaTime);
    }

    private void move(float deltaTime){
        if(velocity.len() == 0)
            return;
        float angle = velocity.angleDeg();
        if(angle > 225 && angle < 315)
            dir = Direction.DOWN;
        else if(angle >= 135 && angle <= 225)
            dir = Direction.LEFT;
        else if(angle > 45 && angle < 135)
            dir = Direction.UP;
        else
            dir = Direction.RIGHT;
        pos.add(velocity.clamp(0, speed*deltaTime));
        velocity.set(0, 0);
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
