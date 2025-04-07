package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.AnimationHandler;

public class Bat extends Enemy {
    public Bat(TiledMapTileMapObject tileObj, AttackableEntity player){
        this(tileObj.getX(), tileObj.getY(), player);
    }

    public Bat(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.animation = new AnimationHandler("pinkbat", dir);
        this.health = 1;
        this.speed = 6f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(2, 2, 12, 10);
        this.attack = new BatAttack();
        this.attackRange = MyGame.TILE_SIZE * 3f;
    }

//    @Override
//    protected void addTransitions(){
//        blueprint.addTransition(State.Idle,     Event.Timeout,  State.Roaming);
//        blueprint.addTransition(State.Roaming,  Event.Timeout,  State.Idle);
//        blueprint.addTransition(State.Stunned,  Event.Timeout,  State.Idle);
//        blueprint.addTransition(State.Dying,    Event.Timeout,  State.Dead);
//    }
//
//    @Override
//    protected void addExitFunctions(){}


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

    public class BatAttack extends Attack{
        private BatAttack(){
            this.damage = 0;
            this.knockback = MyGame.TILE_SIZE * 8;
            this.momentum = speed * 1.5f;
            this.startup = 0.2f;
            this.duration = 0.6f;
            this.cooldown = 1f;
        }

        @Override
        public void placeHitboxes(Vector2 direction){
            this.direction = direction;
            hitboxes.add(new Circle(0, 0, getWidth()/2f));
        }
    }
}
