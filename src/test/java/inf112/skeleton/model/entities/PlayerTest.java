package inf112.skeleton.model.entities;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest{
    @Test
    void testMovement(){
        Player player = new Player(0, 0);
        assertEquals(new Vector2(0, 0), player.pos);

        player.moveRight();
        player.update(1);
        assertTrue(player.getX() > 0);
        assertEquals(0, player.getY());

        player.moveDown();
        player.update(1);
        assertTrue(player.getY() < 0);

        player.moveLeft();
        player.update(2);
        assertTrue(player.getX() < 0);

        player.moveUp();
        player.update(2);
        assertTrue(player.getY() > 0);
    }

    @Test
    void testTravelDistance(){
        Player player = new Player(0, 0);
        player.moveRight();
        player.update(1);
        float distanceStraight = player.getX();

        player.pos = new Vector2(0, 0);
        player.moveLeft();
        player.update(1);
        assertEquals(-distanceStraight, player.getX());

        player.moveDown();
        player.update(1);
        assertEquals(-distanceStraight, player.getY());

        player.pos = new Vector2(0, 0);
        player.moveRight();
        player.moveUp();
        player.update(1);
        assertTrue(player.getX() < distanceStraight);
        assertTrue(player.getY() < distanceStraight);

        float distanceDiagonal = (float) Math.sqrt(Math.pow(player.getX(), 2) + Math.pow(player.getY(), 2));
        assertTrue(Math.abs(distanceStraight - distanceDiagonal) < 0.01f);
    }
}
