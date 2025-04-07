package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.entities.ItemDrop;
import inf112.skeleton.model.inventory.HealthPotion;
import inf112.skeleton.view.AnimationHandler;

public class Dummy extends Enemy{
    public Dummy(TiledMapTileMapObject tileObj, AttackableEntity player){
        this(tileObj.getX(), tileObj.getY(), player);
    }

    public Dummy(float x, float y, AttackableEntity player){
        super(x, y, player);
        animation = new AnimationHandler("char", dir);
        health = 1;
        speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
    }

    @Override
    protected void addTransitions(){
        blueprint.addTransition(State.Idle,     Event.Timeout,  State.Roaming);
        blueprint.addTransition(State.Roaming,  Event.Timeout,  State.Idle);
        blueprint.addTransition(State.Stunned,  Event.Timeout,  State.Idle);
        blueprint.addTransition(State.Dying,    Event.Timeout,  State.Dead);
    }

    @Override
    protected void addExitFunctions(){}

    @Override
    public Array<ItemDrop> getItemDrops(){
        Array<ItemDrop> itemDrops = new Array<>();
//        if(MathUtils.random() <= 0.1f)
            itemDrops.add(new ItemDrop(getCenterX(), getCenterY(), new HealthPotion()));
            itemDrops.add(new ItemDrop(getCenterX(), getCenterY(), new HealthPotion()));
        return itemDrops;
    }
}
