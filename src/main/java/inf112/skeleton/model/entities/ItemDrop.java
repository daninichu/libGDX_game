package inf112.skeleton.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.skeleton.model.inventory.IInventoryPlayer;
import inf112.skeleton.model.inventory.Item;
import inf112.skeleton.util.Box;

public class ItemDrop extends Entity{
    private IInventoryPlayer player;
    private Item item;
    private boolean followPlayer;

    public ItemDrop(TiledMapTileMapObject tileObj, Item item){
        this(tileObj.getX(), tileObj.getY(), item);
    }

    /**
     * The position should be the center position of whoever instantiated this.
     * @param item The item this object will hold.
     */
    public ItemDrop(float centerX, float centerY, Item item) {
        super(centerX, centerY);
        this.item = item;
        texture = item.getTexture();
        hurtbox = new Box(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        pos.sub(getWidth() / 2, getHeight() / 2);
        speed = 60;
        velocity.setToRandomDirection().setLength(speed);
    }

    public void setPlayer(IInventoryPlayer player) {
        this.player = player;
    }

    @Override
    public void update(float deltaTime){
        prevPos.set(pos);
        if(followPlayer){
            speed = Math.min(100, speed + 80 * deltaTime);
            velocity.set(player.getCenterPos().sub(getCenterPos()));
            if(player.locateHurtbox().contains(getCenterPos()))
                if(player.getInventory().addItem(item))
                    item = null;
                else{
                    followPlayer = false;
                    speed = 60;
                    velocity.scl(-1).setLength(speed);
                }
        }
        else {
            speed = Math.max(0, speed - 80 * deltaTime);
            if(getCenterPos().dst(player.getCenterPos()) < 24 && speed == 0)
                followPlayer = true;
        }
        velocity.setLength(speed);
        move(deltaTime);
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
    public TextureRegion getTexture(){
        return texture;
    }
}
