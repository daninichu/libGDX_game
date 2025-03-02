package inf112.skeleton.model.entities.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Sign extends GameObject {
    private String text;

    public Sign(float x, float y, String text) {
        super(x, y);
        this.texture = new Texture("maps/sign.png");
        this.hurtbox = new Rectangle(2, 1, 28, 8);
        this.text = text;
    }
}
