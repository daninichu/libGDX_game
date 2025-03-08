package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.view.ViewableEntity;

public abstract class Enemy extends Entity{
    private static FsmBlueprint blueprint = new FsmBlueprint();
    static {
        blueprint.addTransition("init", "init", "idle");
        blueprint.addTransition("idle", "playerVisible", "chase");
        blueprint.addTransition("idle", "timeout", "roaming");
        blueprint.addTransition("roaming", "timeout", "idle");
        blueprint.addTransition("roaming", "playerVisible", "chase");
        blueprint.addTransition("chase", "playerFar", "idle");
        blueprint.addTransition("chase", "playerClose", "attacking");
        blueprint.addTransition("attacking", "timeout", "chase");
    }

    private StateMachine stateMachine = new StateMachine(blueprint, "init");

    private ViewableEntity player;
    private float timer;
    public static final float vision = MyGame.TILE_SIZE * 8;
    protected float attackRange;

    public Enemy(float x, float y, ViewableEntity player) {
        super(x, y);
        this.player = player;

        stateMachine.onEnter("idle", () -> {
            timer = MathUtils.random(1.2f, 3f);
            velocity.setLength(0);
        });
        stateMachine.onEnter("roaming", () -> {
            timer = MathUtils.random(0.2f, 2);
            velocity.setToRandomDirection();
            velocity.setLength(speed / 2f);
        });
        stateMachine.onEnter("chase", () -> {
            velocity.set(speed, 0);
        });
        stateMachine.onEnter("attacking", () -> {
            timer = 1;
            velocity.setLength(0);
        });
        stateMachine.fireEvent("init");
    }

    @Override
    public void update(float deltaTime) {
        float distance = getCenterPos().dst(player.getCenterPos());
        if (distance <= attackRange)
            stateMachine.fireEvent("playerClose");
        else if (distance <= vision)
            stateMachine.fireEvent("playerVisible");
        else if (distance > vision)
            stateMachine.fireEvent("playerFar");

        if (stateMachine.getState().equals("chase")) {
            float angle = MathUtils.atan2(player.getCenterY() - getCenterY(), player.getCenterX() - getCenterX());
            velocity.setAngleRad(angle);
        }

        prevPos.set(pos);
        move(deltaTime);

        timer -= deltaTime;
        if (timer <= 0) {
            stateMachine.fireEvent("timeout");
        }
    }

    public String getState() {
        return stateMachine.getState();
    }

    public float getAttackRange(){
        return attackRange;
    }
}
