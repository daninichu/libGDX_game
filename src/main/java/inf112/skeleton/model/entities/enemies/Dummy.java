package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.util.Box;

public class Dummy extends Enemy{
    public Dummy(float x, float y, AttackableEntity player){
        super(x, y, player);
        this.texture = new TextureRegion(new Texture("sprite16.png"));
        speed = 2.5f * MyGame.TILE_SIZE;
        this.hurtbox = new Box(0, 0, MyGame.TILE_SIZE, MyGame.TILE_SIZE);
    }

    @Override
    protected void addTransitions(){
        blueprint.addTransition(State.Idle,     Event.Timeout,  State.Roaming);
        blueprint.addTransition(State.Roaming,  Event.Timeout,  State.Idle);
        blueprint.addTransition(State.Stunned,  Event.Timeout,  State.Idle);
    }

    @Override
    protected void addExitFunctions(){}
}
