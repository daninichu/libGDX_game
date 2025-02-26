package inf112.skeleton.model.states;

import inf112.skeleton.model.entities.Player;

public abstract class State{
    protected Player player;
    protected boolean isComplete;

    public State(Player player) {
        this.player = player;
    }

    void enter(){}

    void update(){}

    void exit(){}

}
