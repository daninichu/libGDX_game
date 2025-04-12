package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.util.Box;

public class HealthPotion implements Item {
    private static final TextureRegion tex = new TextureRegion(new Texture("Props_Items/health_potion_item.png"));;

    @Override
    public int heal(){
        return 10;
    }

    @Override
    public TextureRegion getTexture(){
        return tex;
    }

    @Override
    public Box getHurtbox(){
        return new Box(0, 0, tex.getRegionWidth(), tex.getRegionHeight());
    }

    @Override
    public String toString(){
        return "Health Potion";
    }

    @Override
    public String description(){
        return "Restores 10 HP";
    }
}
