package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.DamageableEntity;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.FsmBlueprint;
import inf112.skeleton.model.StateMachine;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.view.ViewableEntity;

import java.util.Random;

public abstract class Enemy extends Entity{
    protected FsmBlueprint blueprint = new FsmBlueprint();
    private StateMachine stateMachine = new StateMachine(blueprint, "idle");

    private DamageableEntity player;
    private float timer;
    public static final float vision = MyGame.TILE_SIZE * 8;
    protected float attackRange;

    public Enemy(float x, float y, DamageableEntity player) {
        super(x, y);
        this.player = player;

        addTransitions();
        addEnterFunctions();
        addExitFunctions();
        stateMachine.fireEvent("idle");
    }

    protected void addTransitions(){
        blueprint.addTransition("idle", "playerVisible", "chase");
        blueprint.addTransition("idle", "timeout", "roaming");
        blueprint.addTransition("roaming", "timeout", "idle");
        blueprint.addTransition("roaming", "playerVisible", "chase");
        blueprint.addTransition("chase", "playerFar", "idle");
        blueprint.addTransition("chase", "playerClose", "attackWindUp");
        blueprint.addTransition("attackWindUp", "timeout", "attacking");
        blueprint.addTransition("attacking", "timeout", "attackEnd");
        blueprint.addTransition("attackEnd", "timeout", "chase");
    }

    protected void addEnterFunctions(){
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
        stateMachine.onEnter("attackWindUp", () -> {
            timer = 0.3f;
            velocity.set(player.getCenterPos().sub(getCenterPos()).setLength(0.1f));
        });
        stateMachine.onEnter("attacking", () -> {
            placeHitboxes();
            timer = 0.4f;
            velocity.setLength(speed*3);
        });
        stateMachine.onEnter("attackEnd", () -> {
            timer = 0.8f;
            velocity.set(0, 0);
        });
    }

    protected void addExitFunctions(){
        stateMachine.onExit("attacking", () -> {
            hitboxes.clear();
        });
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
            float angle = player.getCenterPos().sub(getCenterPos()).angleRad();
            velocity.setAngleRad(angle);
        }

        prevPos.set(pos);
        move(deltaTime);
        attack(player);

        timer -= deltaTime;
        if (timer <= 0) {
            float chance = MathUtils.random.nextFloat();
            if(chance < 0.25f) {
                stateMachine.fireEvent("random");
            }
            else
                stateMachine.fireEvent("timeout");
        }
    }

    protected void placeHitboxes(){}

    public String getState() {
        return stateMachine.getState();
    }

    public float getAttackRange(){
        return attackRange;
    }
}
