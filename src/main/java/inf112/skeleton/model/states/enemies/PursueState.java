package inf112.skeleton.model.states.enemies;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.view.ViewableEntity;

public class PursueState extends EnemyState{
    public static float vision = 32*8;

    public PursueState(Enemy enemy, ViewableEntity player) {
        super(enemy, player);
    }

    @Override
    public void update(float deltaTime){
        Vector2 trueTarget = player.getCenterPos();
        if(trueTarget.dst(enemy.getCenterPos()) > vision){
            enemy.changeState(new PatrolState(enemy, player));
            return;
        }
        enemy.setTarget(trueTarget);
        Vector2 toTarget = trueTarget.sub(enemy.getCenterPos());
        enemy.getVelocity().add(toTarget);
        enemy.moveEnemy(deltaTime);
        enemy.getVelocity().set(0, 0);
    }

    @Override
    public String toString(){
        return "Pursue";
    }
}
