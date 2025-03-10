package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.DamageableEntity;
import inf112.skeleton.view.ViewableEntity;

public class EvilSquare extends Enemy{
    public EvilSquare(float x, float y, DamageableEntity player){
        super(x, y, player);
        texture = new TextureRegion(new Texture("sprite16.png"));
        this.speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Rectangle(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
        this.attackRange = MyGame.TILE_SIZE * 2;
    }

    @Override
    protected void placeHitboxes(){
        Vector2 range = new Vector2(attackRange, 0);
        switch(dir){
            case LEFT -> {
                range.setAngleDeg(180);
                hitboxes.add(new Rectangle(0,0,MyGame.TILE_SIZE, MyGame.TILE_SIZE*2).setCenter(getCenterPos().add(range)));
            }
            case RIGHT -> {
                hitboxes.add(new Rectangle(0,0,MyGame.TILE_SIZE, MyGame.TILE_SIZE*2).setCenter(getCenterPos().add(range)));
            }
            case UP -> {
                range.setAngleDeg(90);
                hitboxes.add(new Rectangle(0,0,MyGame.TILE_SIZE*2, MyGame.TILE_SIZE).setCenter(getCenterPos().add(range)));
            }
            case DOWN -> {
                range.setAngleDeg(270);
                hitboxes.add(new Rectangle(0,0,MyGame.TILE_SIZE*2, MyGame.TILE_SIZE).setCenter(getCenterPos().add(range)));
            }
        }
    }

    @Override
    public Array<Rectangle> getHitboxes(){
        return hitboxes;
    }
}
