package inf112.skeleton.model.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Item {
    int heal();

    TextureRegion getTexture();

    String description();
}
