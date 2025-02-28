package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.view.ViewableEntity;

public class EvilSquare extends Enemy{
    public EvilSquare(float x, float y, ViewableEntity player){
        super(x, y, player);
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attackRange = MyGame.TILE_SIZE * 2;
    }

}
