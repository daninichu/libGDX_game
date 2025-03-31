package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.Direction;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.view.animation.EntityAnimation;

public class Slime extends Enemy {
    public Slime(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.animation = new SlimeAnimation();
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

    public class SlimeAnimation extends EntityAnimation{
        public SlimeAnimation(){
            super("pinkslime", new State[]{State.IDLE, State.RUN, State.HIT, State.DEATH}, Direction.values());
//            for(State state : new State[]{State.IDLE, State.RUN, State.HIT, State.DEATH}){
//                TextureAtlas.AtlasRegion region = atlas.findRegion("pinkslime_"+state.toString().toLowerCase()+"_anim_all_dir_strip");
//                animations.get(state).put(Direction.DOWN, new Animation<>(0.125f, textureToFrames(region)));
//            }
//            setCurrentAnimation();
        }

//        @Override
//        public void setDirection(Direction direction){}
    }
}
