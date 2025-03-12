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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, String>> e1 : transitions.entrySet()) {
            String from = e1.getKey();
            for (Map.Entry<String, String> e2 : e1.getValue().entrySet()) {
                String onEvent = e2.getKey();
                String to = e2.getValue();
                sb.append(from).append(" -> ").append(onEvent).append(" -> ").append(to).append("\n");
            }
        }
        return sb.toString();
    }
}
