package inf112.skeleton.model.collision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EntityCollisionTest {
    EntityCollisionHandler collisionHandler;
    Player player;
    Array<Entity> entities;

    @BeforeEach
    void setUp() {
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
        entities = new Array<>();
        entities.add(player);
        collisionHandler = new EntityCollisionHandler();
    }

    void handleCollision() {
        collisionHandler.updateGrid(entities);
        entities.forEach(e -> collisionHandler.handleCollision(e));
    }

    /**
     * The top and bottom of player and dummy are perfectly aligned.
     * When player moves right, make sure that they push the dummy to the right also.
     */
    @Test
    void testPushDummy() {
        Player dummy = new Player(50, 0);
        entities.add(dummy);

        player.setRightMove(true);
        while(!player.locateHurtbox().overlaps(dummy.locateHurtbox()))
            player.update(0.01f);

        for(int i = 0; i < 1000; i++){
            player.update(0.01f);
            handleCollision();

            assertNotEquals(player.getPrevPos(), player.getPos());
            assertNotEquals(dummy.getPrevPos(), dummy.getPos());
            assertTrue(player.getCenterX() < dummy.getCenterX());
        }
    }

    /**
     * There are 2 dummies right next to each other that are above the player.
     * Ensure that the player can move through them, and that they get pushed to the side.
     */
    @Test
    void testPushThroughDummies() {
        Player leftDummy = new Player(-player.getWidth() / 2, 50);
        Player rightDummy = new Player(player.getWidth() / 2, 50);
        entities.add(leftDummy);
        entities.add(rightDummy);

        assertTrue(player.getTopY() < leftDummy.getBottomY());
        assertTrue(player.getTopY() < rightDummy.getBottomY());

        Vector2 leftStartPos = leftDummy.getPos();
        Vector2 rightStartPos = rightDummy.getPos();

        player.setUpMove(true);
        while(player.getY() < 100){
            player.update(0.01f);
            handleCollision();
        }

        assertTrue(leftStartPos.x > leftDummy.getX());
        assertTrue(leftStartPos.y < leftDummy.getY());

        assertTrue(rightStartPos.x < rightDummy.getX());
        assertTrue(rightStartPos.y < rightDummy.getY());
    }
}
