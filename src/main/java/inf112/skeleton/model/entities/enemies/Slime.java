package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.util.Box;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.view.AnimationHandler;

public class Slime extends Enemy {
    public Slime(TiledMapTileMapObject tileObj, AttackableEntity player, HashGrid<Rectangle> grid){
        this(tileObj.getX(), tileObj.getY(), player, grid);
    }

    public Slime(float x, float y, AttackableEntity player, HashGrid<Rectangle> grid){
        super(x, y, player, grid);
        this.animation = new AnimationHandler("pinkslime", dir);
        this.hp = 10;

        this.attack = new SlimeAttack();
        this.speed = 1.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(3, 2, 10, 10);
    }

    @Override
    protected void placeHitboxes(){}

    @Override
    public Array<Circle> getHitboxes(){
        switch(getState()){
            case Stunned, Dying, Dead -> {
                return new Array<>();
            }
        }
        return new Array<>(new Circle[]{new Circle(getCenterPos(), 8)});
    }

    private class SlimeAttack extends Attack{
        private SlimeAttack(){
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
