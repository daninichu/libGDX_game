package inf112.skeleton.model;

import java.util.HashMap;
import java.util.Map;

public class FsmBlueprint {
    private final Map<String, Map<String, String>> transitions = new HashMap<>();

    public void addTransition(String from, String onEvent, String to) {
        transitions.computeIfAbsent(from, k -> new HashMap<>()).put(onEvent, to);
    }

    public String getTargetState(String from, String event) {
        Map<String, String> transitionsFromState = transitions.get(from);
        if (transitionsFromState != null) {
            return transitionsFromState.get(event);
        }
        return null;
    }
}
