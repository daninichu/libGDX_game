package inf112.skeleton.model.collision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class StaticCollisionTest {
    Player player;
    Array<Rectangle> collisionBoxes;

    @BeforeEach
    void setUp() {
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
        collisionBoxes = new Array<>();
    }

    /**
     * Player starts far from the box and moves to the right.
     * When collision occurs, make sure that the player backtracks to a non-collision state.
     */
    @Test
    void testStopWhenCollides() {
        collisionBoxes.add(new Rectangle(50, 0, 100, 100));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        player.setRightMove(true);
        while(!StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes)){
            player.update(0.01f);
            assertNotEquals(player.getPrevPos(), player.getPos());
        }

        collisionHandler.handleCollision(player);
        assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
    }

    /**
     * Player starts right above a box. Make sure that the player can't move down.
     */
    @Test
    void testCantMoveDown() {
        collisionBoxes.add(new Rectangle(0, -50, 100, 50));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));

        Vector2 stuckPos = player.getPos().cpy();
        player.setDownMove(true);
        for(int i = 0; i < 1000; i++){
            player.update(0.01f);
            collisionHandler.handleCollision(player);

            assertEquals(stuckPos, player.getPos());
            assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
        }
    }

    /**
     * Player is right next to, and right above a box.
     * Make sure that the player can't move left and down diagonally.
     */
    @Test
    void testCantMoveDiagonally() {
        collisionBoxes.add(new Rectangle(-50, 0, 50, 100));
        collisionBoxes.add(new Rectangle(0, -50, 100, 50));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));

        Vector2 stuckPos = player.getPos().cpy();
        player.setLeftMove(true);
        player.setDownMove(true);
        for(int i = 0; i < 1000; i++){
            player.update(0.01f);
            collisionHandler.handleCollision(player);

            assertEquals(stuckPos, player.getPos());
            assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
        }
    }

    /**
     * Player starts right above a box and tries to move diagonally against it.
     * Make sure that the player can move sideways, but not downwards.
     */
    @Test
    void testSlideAgainstBox() {
        collisionBoxes.add(new Rectangle(0, -50, 1000, 50));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));

        float stuckY = player.getY();
        player.setRightMove(true);
        player.setDownMove(true);
        while(player.getX() < 1000){
            assertEquals(stuckY, player.getY());

            player.update(0.01f);
            collisionHandler.handleCollision(player);

            assertNotEquals(player.getPrevPos(), player.getPos());
            assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
        }
    }

    /**
     * Player starts right above boxes with aligned tops, and with gaps too small for the player.
     * Player tries to move diagonally against them.
     * Make sure that the player can move sideways, and doesn't slip through the gaps.
     */
    @Test
    void testMoveAcrossGaps() {
        collisionBoxes.add(new Rectangle(0, -50, 10, 50));
        collisionBoxes.add(new Rectangle(-20, -50, 10, 50));
        collisionBoxes.add(new Rectangle(-40, -50, 10, 50));
        collisionBoxes.add(new Rectangle(-60, -50, 10, 50));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        float stuckY = player.getY();
        player.setLeftMove(true);
        player.setDownMove(true);
        while(player.getX() > -60){ // While player x > leftmost box x
            assertEquals(stuckY, player.getY());

            player.update(0.01f);
            collisionHandler.handleCollision(player);

            assertNotEquals(player.getPrevPos(), player.getPos());
            assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
        }
    }

    /**
     * There are 2 boxes with aligned x-coordinates with a gap big enough for the player.
     * Player tries to move diagonally against them towards the gap.
     * Make sure that the player slips through the gap.
     */
    @Test
    void testSlipThroughGap(){
        collisionBoxes.add(new Rectangle(-100, 0, 100, 100));
        collisionBoxes.add(new Rectangle(-100, 150, 100, 100));
        StaticCollisionHandler collisionHandler = new StaticCollisionHandler(collisionBoxes);

        float stuckX = player.getX();
        player.setLeftMove(true);
        player.setUpMove(true);
        while(player.getY() < 100){ // While player bottom < box top
            assertEquals(stuckX, player.getX());

            player.update(0.01f);
            collisionHandler.handleCollision(player);

            assertNotEquals(player.getPrevPos(), player.getPos());
            assertFalse(StaticCollisionHandler.collidesAny(player.locateHurtbox(), collisionBoxes));
        }

        for(int i = 0; i < 100; i++){
            player.update(0.01f);
            collisionHandler.handleCollision(player);
            assertTrue(player.getX() < player.getPrevPos().x);
        }
    }
}
