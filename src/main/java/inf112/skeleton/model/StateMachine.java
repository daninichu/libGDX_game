package inf112.skeleton.model;

import java.util.HashMap;
import java.util.Map;

public class StateMachine<S, T>{
    private final FsmBlueprint<S, T> blueprint;
    private S state;
    private Map<S, Runnable> onEnter = new HashMap<>();
    private Map<S, Runnable> onExit = new HashMap<>();

    public StateMachine(FsmBlueprint<S, T> blueprint, S initialState) {
        this.blueprint = blueprint;
        this.state = initialState;
    }

    public S getState() {
        return state;
    }

    public void fireEvent(T event) {
        S targetState = blueprint.getTargetState(state, event);
        if (targetState == null || targetState.equals(state)) {
            return;
        }
        transition(targetState);
    }

    public void onEnter(S event, Runnable action) {
        onEnter.put(event, action);
    }

    public void onExit(S event, Runnable action) {
        onExit.put(event, action);
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
