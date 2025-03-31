package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.Direction;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;
import inf112.skeleton.view.animation.EntityAnimation;

public class Phantom extends Enemy{
    public Phantom(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.animation = new PhantomAnimation();
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        this.health = 30;
//        this.mass = 1;
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attack = new PhantomAttack();
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

    public class PhantomAttack extends Attack{
        private Circle baseHitbox = new Circle(0, 0, getWidth()/2f);

        private PhantomAttack(){
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

    public class PhantomAnimation extends EntityAnimation{
        public PhantomAnimation(){
            super("phantom", new State[]{State.IDLE, State.RUN, State.HIT, State.DEATH}, new Direction[]{Direction.LEFT, Direction.RIGHT});

//            filePaths.put(State.ATTACK, "run_");
//
//            for (Direction d : Direction.values()){
//                if(d == Direction.UP || d == Direction.DOWN){
//                    continue;
//                }
//                String dir = d.toString().toLowerCase();
//                for(State state : State.values()){
//                    String file = filePaths.get(state);
//                    TextureAtlas.AtlasRegion region = atlas.findRegion("phantom_" + file + "anim_" + dir + "_strip");
//                    animations.get(state).put(d, new Animation<>(0.125f, textureToFrames(region)));
//                }
//            }
//            setDirection(Direction.RIGHT);
//            setCurrentAnimation();
        }
    }
}
