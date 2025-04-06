package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.view.AnimationHandler;

public class Slime extends Enemy {
    public Slime(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.animation = new AnimationHandler("pinkslime", dir);
        this.health = 15;

        this.attack = new SlimeAttack();
        this.speed = 1.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(2, 2, 12, 10);
    }

    @Override
    public Array<Circle> getHitboxes(){
        switch(stateMachine.getState()){
            case Dying, Dead -> {
                return new Array<>();
            }
        }
        return new Array<>(new Circle[]{new Circle(getCenterPos(), 8)});
    }

    public class SlimeAttack extends Attack{
        public SlimeAttack(){
            this.damage = 2;
            this.knockback = MyGame.TILE_SIZE * 4;
        }

        @Override
        public void addHit(AttackableEntity target){}

        @Override
        public Vector2 knockbackVector(Vector2 targetPos){
            return targetPos.sub(getCenterPos()).setLength(knockback);
        }
    }
}
