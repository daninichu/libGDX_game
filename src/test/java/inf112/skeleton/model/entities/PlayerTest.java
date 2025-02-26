package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest{
    Player player;

    @BeforeEach
    void setup(){
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
        player.update(1);
        player.setRightMove(false);

        player.pos = new Vector2(0, 0);
        player.setLeftMove(true);
        player.update(1);
        player.setLeftMove(false);
        assertEquals(-player.speed, player.getX());

        player.setDownMove(true);
        player.update(1);
        player.setDownMove(false);
        assertEquals(-player.speed, player.getY());

        player.pos = new Vector2(0, 0);
        player.setRightMove(true);
        player.setUpMove(true);
        player.update(1);
        assertTrue(player.getX() < player.speed);
        assertTrue(player.getY() < player.speed);

        float distanceDiagonal = (float) Math.sqrt(Math.pow(player.getX(), 2) + Math.pow(player.getY(), 2));
        assertTrue(Math.abs(player.speed - distanceDiagonal) < 0.01f);
    }
}
