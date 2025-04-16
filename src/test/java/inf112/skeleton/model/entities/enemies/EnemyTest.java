package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.collision.StaticCollisionHandler;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EnemyTest{
    Player player;
    StaticCollisionHandler grid = new StaticCollisionHandler();

    @BeforeEach
    public void setUp() {
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
        grid.updateGrid(new Array<>());
    }

    @Test
    void testInitialState() {
        Enemy enemy = new Phantom(300, 0, player, grid);
        assertEquals(Enemy.State.Idle, enemy.getState());
    }

    @Test
    void testRoaming(){
        Enemy enemy = new Phantom(300, 0, player, grid);
        for(int i = 0; i < 1000; i++){
            if(!enemy.getState().equals(Enemy.State.Idle))
                break;
            enemy.update(0.1f);
        }
        assertEquals(Enemy.State.Roaming, enemy.getState());

        Vector2 oldPos = enemy.getPos();
        for(int i = 0; i < 1000; i++)
            enemy.update(0.1f);
        assertNotEquals(oldPos, enemy.getPos());
    }

    @Test
    void testSeesPlayer(){
        Enemy enemy = new Phantom(300, 0, player, grid);
        assertNotEquals(Enemy.State.Chase, enemy.getState());

        player.setRightMove(true);
        while(player.getCenterPos().dst(enemy.getCenterPos()) > Enemy.vision)
            player.update(0.1f);
        enemy.update(0.1f);

        assertNotNull(enemy.getRay());
        assertEquals(Enemy.State.Chase, enemy.getState());
    }

    @Test
    void testDoesntSeePlayer(){
        Enemy enemy = new Phantom(100, 0, player, grid);

        enemy.update(0.1f);
        assertNotNull(enemy.getRay());
        assertEquals(Enemy.State.Chase, enemy.getState());

        player.setPos(1000, 0);
        enemy.update(0.1f);
        assertNull(enemy.getRay());

        for(int i = 0; i < 10000; i++){
            enemy.update(0.1f);
            if(enemy.getState().equals(Enemy.State.Idle))
                return;
        }
        fail("Enemy never transitions to idle state");
    }

    @Test
    void testAttackPlayer(){
        Enemy enemy = new Phantom(100, 0, player, grid);

        while(enemy.getCenterPos().dst(player.getCenterPos()) > enemy.attackRange)
            enemy.update(0.1f);
        enemy.update(0.1f);

        assertEquals(Enemy.State.AttackStartup, enemy.getState());
    }
}
