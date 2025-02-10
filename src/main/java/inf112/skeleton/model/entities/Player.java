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
        if(dx != 0 && dy != 0){
            dx /= (float) Math.sqrt(2);
            dy /= (float) Math.sqrt(2);
        }
        if(dy > 0)
            dir = Direction.UP;
        if(dy < 0)
            dir = Direction.DOWN;
        if(dx > 0)
            dir = Direction.RIGHT;
        if(dx < 0)
            dir = Direction.LEFT;
        pos.add(dx*deltaTime, dy*deltaTime);
        dx = dy = 0;
    }

    @Override
    public void moveRight(){
        dx += speed;
    }

    @Override
    public void moveLeft(){
        dx -= speed;
    }

    @Override
    public void moveUp(){
        dy += speed;
    }

    @Override
    public void moveDown(){
        dy -= speed;
    }
}
