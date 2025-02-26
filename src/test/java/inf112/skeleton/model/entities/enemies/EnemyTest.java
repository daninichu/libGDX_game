package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.states.enemies.PatrolState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest{
    Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0);
    }

    @Test
    void TestPatrol() {
        Enemy enemy = new EvilSquare(300, 0, player);
        assertEquals("Patrol", enemy.currentState());

        Vector2 start = new Vector2(enemy.getX(), enemy.getY());
        for(int i = 0; i < 10; i++){
            enemy.update(1);
        }
        assertNotEquals(start, new Vector2(enemy.getX(), enemy.getY()));
    }

    @Test
    void testPursue(){
        Enemy enemy = new EvilSquare(200, 0, player);
        enemy.update(1);
        assertEquals("Pursue", enemy.currentState());

        float oldDist = player.getCenterPos().dst(enemy.getCenterPos());
        for(int i = 0; i < 10; i++){
            enemy.update(1);
        }
        assertTrue(player.getCenterPos().dst(enemy.getCenterPos()) < oldDist);
    }

    @Test
    void testPatrolToPursue(){
        Enemy enemy = new EvilSquare(300, 0, player);
        assertEquals("Patrol", enemy.currentState());
        player.setRightMove(true);
        while(player.getCenterPos().dst(enemy.getCenterPos()) > PatrolState.vision){
            player.update(1);
        }
        enemy.update(1);
        assertEquals("Pursue", enemy.currentState());
    }

    @Test
    void testPursueToPatrol(){
        Enemy enemy = new EvilSquare(200, 0, player);
        enemy.update(1);
        assertEquals("Pursue", enemy.currentState());

        player.setLeftMove(true);
        while(player.getCenterPos().dst(enemy.getCenterPos()) <= PatrolState.vision){
            player.update(1);
            enemy.update(1);
        }
        enemy.update(1);
        assertEquals("Patrol", enemy.currentState());
    }

}
