package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthPotion implements Item {
    private static final TextureRegion texture = new TextureRegion(new Texture("Props_Items/health_potion_item.png"));;

    @Override
    public int heal(){
        return 10;
    }

    @Override
    public TextureRegion getTexture(){
        return texture;
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
