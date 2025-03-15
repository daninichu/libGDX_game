package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.DamageableEntity;
import inf112.skeleton.view.ViewableEntity;

public class EvilSquare extends Enemy{

    public EvilSquare(float x, float y, DamageableEntity player){
        super(x, y, player);
        texture = new TextureRegion(new Texture("sprite16.png"));
        this.mass = 1;
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attackRange = MyGame.TILE_SIZE * 2;
    }

    @Override
    protected void placeHitboxes(){
        hitboxes.add(hurtbox);
    }

    @Override
    public Array<Rectangle> getHitboxes(){
        Array<Rectangle> result = new Array<>();
        for(Rectangle hitbox : hitboxes){
            Rectangle adjustedHitbox = new Rectangle(hitbox);
            result.add(adjustedHitbox.setPosition(pos.x + adjustedHitbox.x, pos.y + adjustedHitbox.y));
        }
        return result;
    }
}
