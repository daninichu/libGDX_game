package inf112.skeleton.model.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PlayerTest{
    Player player;

    @BeforeEach
    public void setUp() {
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
    }

    @Test
    void testMoveRight(){
        player.setRightMove(true);
        player.update(1);
        assertEquals(new Vector2(player.speed, 0), player.pos);
    }

    @Test
    void testMoveLeft(){
        player.setLeftMove(true);
        player.update(1);
        assertEquals(new Vector2(-player.speed, 0), player.pos);
    }

    @Test
    void testMoveUp(){
        player.setUpMove(true);
        player.update(1);
        assertEquals(new Vector2(0, player.speed), player.pos);
    }

    @Test
    void testMoveDown(){
        player.setDownMove(true);
        player.update(1);
        assertEquals(new Vector2(0, -player.speed), player.pos);
    }

    @Test
    void testTravelDistance(){
        player.setRightMove(true);
        player.setUpMove(true);
        player.update(1);
        assertTrue(player.getX() < player.speed);
        assertTrue(player.getY() < player.speed);
        assertEquals(player.pos.len(), player.speed);
    }
}
