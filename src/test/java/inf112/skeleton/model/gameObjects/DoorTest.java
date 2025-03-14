package inf112.skeleton.model.gameObjects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.gameObjects.Door;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DoorTest {
    Player player;
    Door door1, door2;

    @BeforeEach
    void setup(){
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);

        TiledMap tempMap = new TmxMapLoader().load("tiledMaps/doorTest.tmx");
        MapObjects doors = tempMap.getLayers().get(0).getObjects();
        door1 = new Door((TiledMapTileMapObject) doors.get(0), player);
        door2 = new Door((TiledMapTileMapObject) doors.get(1), player);
    }

    @Test
    void testPositions(){
        assertEquals(new Vector2(32, 32), door1.getPos());
        assertEquals(new Vector2(224, 32), door2.getPos());
    }

    /**
     * Player starts out of interaction range of both doors.<br>
     * When the player moves to the right, they should be in interaction range of door1
     * and not door2. Move some more, then in interaction range of door2 and not door1.
     */
    @Test
    void testInteractionRange() {
        assertFalse(door1.inInteractionRange());
        assertFalse(door2.inInteractionRange());

        player.setRightMove(true);
        while(player.getCenterX() < door1.locateHurtbox().x)
            player.update(0.1f);
        assertTrue(door1.inInteractionRange());
        assertFalse(door2.inInteractionRange());

        while(player.getCenterX() < door2.locateHurtbox().x)
            player.update(0.1f);
        assertFalse(door1.inInteractionRange());
        assertTrue(door2.inInteractionRange());
    }

    /**
     * When the player interacts with door1, they should be teleported to door2, and vice versa.
     */
    @Test
    void testDoorExitPoints(){
        player.setPos(door1.getExitPos());
        assertFalse(door1.inInteractionRange());
        assertTrue(door2.inInteractionRange());

        player.setPos(door2.getExitPos());
        assertFalse(door2.inInteractionRange());
        assertTrue(door1.inInteractionRange());
    }
}
