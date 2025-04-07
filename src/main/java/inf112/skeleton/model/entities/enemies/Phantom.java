package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.Attack;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.entities.ItemDrop;
import inf112.skeleton.model.inventory.HealthPotion;
import inf112.skeleton.view.AnimationHandler;

public class Phantom extends Enemy{
    public Phantom(TiledMapTileMapObject tileObj, AttackableEntity player){
        this(tileObj.getX(), tileObj.getY(), player);
    }

    public Phantom(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.animation = new AnimationHandler("phantom", dir);
        this.health = 20;
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(2, 2, 12, 12);
        this.attack = new PhantomAttack();
        this.attackRange = MyGame.TILE_SIZE * 3;
    }

    @Override
    public Array<ItemDrop> getItemDrops(){
        Array<ItemDrop> itemDrops = new Array<>();
        if(MathUtils.random() <= 0.5f)
            itemDrops.add(new ItemDrop(getCenterX(), getCenterY(), new HealthPotion()));
        return itemDrops;
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
}
