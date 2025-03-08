package inf112.skeleton.model.entities.enemies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EnemyTest{
    Player player;

    @BeforeEach
    public void setUp() {
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
    }

    @Test
    void testIdle() {
        Enemy enemy = new EvilSquare(300, 0, player);
        assertEquals("idle", enemy.getState());
    }

    @Test
    void testRoaming(){
        Enemy enemy = new EvilSquare(300, 0, player);
        for(int i = 0; i < 10; i++){
            if(!enemy.getState().equals("idle"))
                break;
            enemy.update(1);
        }
        assertEquals("roaming", enemy.getState());

        Vector2 oldPos = new Vector2(enemy.getX(), enemy.getY());
        for(int i = 0; i < 10; i++){
            enemy.update(1);
        }
        assertNotEquals(oldPos, new Vector2(enemy.getX(), enemy.getY()));
    }

    @Test
    void testTransitionToChase(){
        Enemy enemy = new EvilSquare(300, 0, player);
        assertNotEquals("chase", enemy.getState());
        player.setRightMove(true);
        while(player.getCenterPos().dst(enemy.getCenterPos()) > Enemy.vision){
            player.update(1);
        }
        enemy.update(1);
        assertEquals("chase", enemy.getState());
    }

    @Test
    void testChaseToIdle(){
        Enemy enemy = new EvilSquare(100, 0, player);
        enemy.update(1);
        assertEquals("chase", enemy.getState());

        player.setLeftMove(true);
        while(player.getCenterPos().dst(enemy.getCenterPos()) <= Enemy.vision){
            player.update(1);
            enemy.update(1);
        }
        assertEquals("idle", enemy.getState());
    }

}
