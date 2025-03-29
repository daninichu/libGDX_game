package inf112.skeleton.model.entities.enemies;

import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.attack.AttackableEntity;
import inf112.skeleton.model.Box;

public class Dummy extends Enemy{
    public Dummy(float x, float y, AttackableEntity player){
        super(x, y, player);
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
