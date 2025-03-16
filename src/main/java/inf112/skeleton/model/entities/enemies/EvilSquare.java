package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;

public class EvilSquare extends Enemy{

    public EvilSquare(float x, float y, AttackableEntity player){
        super(x, y, player);
        texture = new TextureRegion(new Texture("sprite16.png"));
        this.health = 30;
        this.mass = 1;
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attack = new EvilSquareAttack();
        this.attackRange = MyGame.TILE_SIZE * 3;
    }

    @Override
    protected void placeHitboxes(){
        attack.placeHitboxes(velocity.cpy());
    }

    @Override
    public Array<Rectangle> getHitboxes(){
        Array<Rectangle> result = new Array<>();
        for(Rectangle hitbox : attack.getHitboxes()){
            Rectangle adjustedHitbox = new Rectangle(hitbox);
            adjustedHitbox.setCenter(getCenterPos());
            result.add(adjustedHitbox);
        }
        return result;
    }

    public static class EvilSquareAttack extends Attack{
        private Rectangle baseHitbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);

        private EvilSquareAttack(){
            this.damage = 2;
            this.knockback = MyGame.TILE_SIZE * 2;
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            angle = direction.angleDeg();
            hitboxes.add(baseHitbox);
        }
    }
}
