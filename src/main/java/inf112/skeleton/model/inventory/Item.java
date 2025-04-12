package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.util.Box;

public interface Item {
    default int heal(){
        return 0;
    }

    default float speedMultiplier(){
        return 1;
    }

    TextureRegion getTexture();

    Box getHurtbox();

    String description();
}
