package inf112.skeleton.model;

import java.util.HashMap;
import java.util.Map;

public class StateMachine{
    private final FsmBlueprint blueprint;
    private String state;
    private Map<String, Runnable> onEnter = new HashMap<>();
    private Map<String, Runnable> onExit = new HashMap<>();

    public StateMachine(FsmBlueprint blueprint, String initialState) {
        this.blueprint = blueprint;
        this.state = initialState;
    }

    public String getState() {
        return state;
    }

    public void fireEvent(String event) {
        String targetState = blueprint.getTargetState(state, event);
        if (targetState == null || targetState.equals(state)) {
            return;
        }
        transition(targetState);
    }

    public void onEnter(String event, Runnable action) {
        onEnter.put(event, action);
    }

    public void onExit(String event, Runnable action) {
        onExit.put(event, action);
    }

    public void forceState(String targetState) {
        transition(targetState);
    }

    private void transition(String targetState){
        Runnable onExitAction = onExit.get(state);
        if (onExitAction != null)
            onExitAction.run();
        Runnable onEnterAction = onEnter.get(targetState);
        if (onEnterAction != null)
            onEnterAction.run();
        state = targetState;
    }
}
