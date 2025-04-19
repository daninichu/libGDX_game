package inf112.skeleton.model.attack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AttackTest {
    Player player;

    Entity attacker = new Entity(0, 0){
        @Override
        public Array<Circle> getHitboxes(){
            attack.damage = 3;
            attack.knockback = 100;
            attack.direction = new Vector2(1, 0);

            Circle[] hitboxes = new Circle[]{new Circle(0, 0, 50)};
            return new Array<>(hitboxes);
        }
    };

    @BeforeEach
    void setUp(){
        new HeadlessApplication(new ApplicationAdapter(){});
        Gdx.gl = mock(GL20.class);
        player = new Player(0, 0);
    }

    @Test
    void testDamage(){
        float maxHP = player.getHp();
        player.getAttacked(attacker);
        assertEquals(maxHP - 3, player.getHp());
    }

    @Test
    void testKnockback(){
        Vector2 startPos = player.getPos();
        player.getAttacked(attacker);
        player.update(0.1f);

        assertTrue(startPos.x < player.getX());
    }

    /**
     * Player is barely avoiding a hitbox. Make sure that when the player moves a bit to the left, they get hit.
     */
    @Test
    void testAttackRange(){
        player.setPos(50 + player.getX() - player.getLeftX(), 0);

        float maxHP = player.getHp();
        player.getAttacked(attacker);
        assertEquals(maxHP, player.getHp());

        player.addPos(-1, 0);
        player.getAttacked(attacker);
        assertEquals(maxHP - 3, player.getHp());
    }
}
