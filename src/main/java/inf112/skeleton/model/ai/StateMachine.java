package inf112.skeleton.model.ai;

import java.util.HashMap;
import java.util.Map;

public class StateMachine<S, E>{
    private FsmBlueprint<S, E> blueprint;
    private Map<S, Runnable> onEnter = new HashMap<>();
    private Map<S, Runnable> onExit = new HashMap<>();
    private S state;

    public StateMachine(FsmBlueprint<S, E> blueprint, S initialState) {
        this.blueprint = blueprint;
        this.state = initialState;
    }

    public void onEnter(S event, Runnable action) {
        onEnter.put(event, action);
    }

    public void onExit(S event, Runnable action) {
        onExit.put(event, action);
    }

    public S getState() {
        return state;
    }

    public void fireEvent(E event) {
        S targetState = blueprint.getTargetState(state, event);
        if(targetState != null && !targetState.equals(state))
            transition(targetState);
    }

    public void forceState(S targetState) {
        transition(targetState);
    }

    private void transition(S targetState){
        Runnable onExitAction = onExit.get(state);
        if (onExitAction != null)
            onExitAction.run();
        Runnable onEnterAction = onEnter.get(targetState);
        if (onEnterAction != null)
            onEnterAction.run();
        state = targetState;
    }
}
