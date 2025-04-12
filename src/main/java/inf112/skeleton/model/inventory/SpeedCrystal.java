package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.util.Box;

public class SpeedCrystal implements Item{
    private static final TextureRegion tex = new TextureRegion(new Texture("Props_Items/crystal_item_anim_strip_6.png"));

    @Override
    public TextureRegion getTexture(){
        tex.setRegion(0,0,16,16);
        return tex;
    }

    @Override
    public Box getHurtbox(){
        return new Box(4, 4, 8, 8);
    }

    @Override
    public String description(){
        return "";
    }
}
