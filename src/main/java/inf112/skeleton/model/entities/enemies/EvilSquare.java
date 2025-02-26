package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Rectangle;
import inf112.skeleton.view.ViewableEntity;

public class EvilSquare extends Enemy{
    public EvilSquare(float x, float y, ViewableEntity player){
        super(x, y, 32*2, player);
        this.speed = 70;
        this.hurtbox = new Rectangle(0, 0, 32, 32);
    }

}
