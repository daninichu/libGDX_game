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
import inf112.skeleton.app.MyGame;
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
        assertEquals(new Vector2(0, 96), door1.getPos());
        assertEquals(new Vector2(224, 96), door2.getPos());
    }

    /**
     * Player starts out of interaction range of both doors.<br></br>
     * When the player moves up, they should be in interaction range of {@code door1} and not {@code door2}. <br></br>
     * Then move right to be in interaction range of {@code door2} and not {@code door1}.
     */
    @Test
    void testInteractionRange() {
        assertFalse(door1.inInteractionRange());
        assertFalse(door2.inInteractionRange());

        player.setUpMove(true);
        while(player.getCenterY() + player.getHeight() < door1.getY())
            player.update(0.1f);
        assertTrue(door1.inInteractionRange());
        assertFalse(door2.inInteractionRange());

        player.setUpMove(false);
        player.setRightMove(true);
        while(player.getCenterX() < door2.locateHurtbox().x)
            player.update(0.1f);
        assertFalse(door1.inInteractionRange());
        assertTrue(door2.inInteractionRange());
    }

    /**
     * When the player interacts with {@code door1}, they should be teleported to {@code door2}, and vice versa.
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
