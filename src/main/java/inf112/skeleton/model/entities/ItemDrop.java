package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.model.Box;
import inf112.skeleton.model.inventory.HealthPotion;
import inf112.skeleton.model.inventory.IInventoryPlayer;
import inf112.skeleton.model.inventory.Item;

public class ItemDrop extends Entity{
    private IInventoryPlayer player;
    private Item item = new HealthPotion();
    private float acceleration = 100;
    private boolean followPlayer;

    public ItemDrop(float x, float y, IInventoryPlayer player) {
        super(x, y);
        this.player = player;
        texture = new TextureRegion(new Texture("Props_Items/health_potion_item.png"));
        hurtbox = new Box(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
    }

    @Override
    public void update(float deltaTime){
        float distance = getCenterPos().dst(player.getCenterPos());
        if(distance < 32){
            followPlayer = true;
        }
        if(followPlayer){
            speed = Math.min(100, speed + acceleration * deltaTime);
            velocity.set(player.getCenterPos().sub(getCenterPos())).setLength(speed);
        }
        move(deltaTime);
        if(locateHurtbox().overlaps(player.locateHurtbox())){
            player.getInventory().addItem(item);
            item = null;
        }
    }

    @Override
    public boolean dead(){
        return item == null;
    }

    @Override
    public void setPos(float x, float y){
        if(!followPlayer)
            super.setPos(x, y);
    }
    @Override
    public void addPos(float x, float y){
        if(!followPlayer)
            super.addPos(x, y);
    }

    @Override
    public TextureRegion getTexture(){
        return texture;
    }
}
