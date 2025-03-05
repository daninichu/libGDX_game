package inf112.skeleton.model.entities;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.CollisionChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionTest {
    Array<Rectangle> collisionBoxes;
    Player player;

    @BeforeEach
    public void setUp() {
        collisionBoxes = new Array<>();
        player = new Player(0, 0);
    }

    @Test
    public void testCollision() {
        collisionBoxes.add(new Rectangle(50, 0, 100, 100));
        CollisionChecker collisionChecker = new CollisionChecker(collisionBoxes);

        player.setRightMove(true);
        while(!player.locateHurtbox().overlaps(collisionBoxes.first())) {
            player.update(1);
        }
        collisionChecker.checkCollisions(player);
        assertFalse(player.locateHurtbox().overlaps(collisionBoxes.first()));
    }

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new MyGame();
        new HeadlessApplication(listener, config);
    }

}
