package inf112.skeleton.model;

import java.util.HashMap;
import java.util.Map;

public class FsmBlueprint<S, E> {
    private final Map<S, Map<E, S>> transitions = new HashMap<>();

    public void addTransition(S from, E onEvent, S to) {
        transitions.computeIfAbsent(from, k -> new HashMap<>()).put(onEvent, to);
    }

    public S getTargetState(S from, E event) {
        Map<E, S> transitionsFromState = transitions.get(from);
        if (transitionsFromState != null)
            return transitionsFromState.get(event);
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<S, Map<E, S>> e1 : transitions.entrySet()) {
            S from = e1.getKey();
            for (Map.Entry<E, S> e2 : e1.getValue().entrySet()) {
                E onEvent = e2.getKey();
                S to = e2.getValue();
                sb.append(from).append(" -> ").append(onEvent).append(" -> ").append(to).append("\n");
            }
        }
        return sb.toString();
    }
}
