package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;

public class DarkSquare extends Enemy {
    public DarkSquare(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.health = 30;
        this.mass = 1;
        this.speed = 2f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attack = new DarkSquare.DarkSquareAttack();
        this.attackRange = MyGame.TILE_SIZE * 2.5f;
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

    public class DarkSquareAttack extends Attack{
        private DarkSquareAttack(){
            this.damage = 6;
            this.knockback = MyGame.TILE_SIZE * 8;
            this.startup = 1f;
            this.duration = 1f;
            this.cooldown = 1.5f;
        }

        @Override
        public Vector2 knockbackVector(Vector2 targetPos){
            return targetPos.sub(getCenterPos()).setLength(knockback);
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            this.direction = direction;
            hitboxes.add(new Circle(0, 0, MyGame.TILE_SIZE * 3f));
        }
    }
}
