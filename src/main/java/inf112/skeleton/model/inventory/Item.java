package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Item {
    default int heal(){
        return 0;
    }

    TextureRegion getTexture();

    String description();
}
