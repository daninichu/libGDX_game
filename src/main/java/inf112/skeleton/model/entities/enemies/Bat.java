package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.util.Box;
import inf112.skeleton.view.AnimationHandler;

public class Bat extends Enemy {
    public Bat(TiledMapTileMapObject tileObj, AttackableEntity player, HashGrid<Rectangle> grid){
        this(tileObj.getX(), tileObj.getY(), player, grid);
    }

    public Bat(float x, float y, AttackableEntity player, HashGrid<Rectangle> grid){
        super(x, y, player, grid);
        this.animation = new AnimationHandler("pinkbat", dir);
        this.hp = 1;
        this.speed = 5.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(2, 2, 12, 10);
        this.attack = new BatAttack();
        this.attackRange = MyGame.TILE_SIZE * 3.5f;
    }

    private class BatAttack extends Attack{
        private BatAttack(){
            this.damage = 1;
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
