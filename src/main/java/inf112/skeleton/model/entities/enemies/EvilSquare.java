package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;

public class EvilSquare extends Enemy{
    public EvilSquare(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.health = 30;
//        this.mass = 1;
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attack = new EvilSquareAttack();
        this.attackRange = MyGame.TILE_SIZE * 3;
    }

    @Override
    protected void placeHitboxes(){
        attack.placeHitboxes(velocity.cpy());
    }

    @Override
    public Array<Circle> getHitboxes(){
        Array<Circle> result = new Array<>();
        for(Circle hitbox : attack.getHitboxes()){
            Circle adjustedHitbox = new Circle(hitbox);
            adjustedHitbox.setPosition(getCenterPos());
            result.add(adjustedHitbox);
        }
        return result;
    }

    public class EvilSquareAttack extends Attack{
        private Circle baseHitbox = new Circle(0, 0, getWidth()/2f);

        private EvilSquareAttack(){
            this.damage = 2;
            this.knockback = MyGame.TILE_SIZE * 8;
            this.momentum = speed * 4;
            this.startup = 0.3f;
            this.duration = 0.4f;
            this.cooldown = 0.8f;
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            this.direction = direction;
            hitboxes.add(baseHitbox);
        }
    }
}
