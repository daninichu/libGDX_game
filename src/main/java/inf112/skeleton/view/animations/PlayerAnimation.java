package inf112.skeleton.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.Direction;

public class PlayerAnimation extends EntityAnimation {
    public PlayerAnimation() {
        putOffsets();

        float frameDuration = 0.15f;

        int animationFrames = 6;

        String filePath = "Char_Sprites/";
        String walkFile = "char_run_";
        String idleFile = "char_idle_";
        String attackFile = "char_attack_";
        String anim = "_anim_strip_6.png";

        for (var d : Direction.values()) {
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(Gdx.files.internal(filePath + walkFile + dir + anim)));
            walkAnimation.set(d.ordinal(), new Animation<>(frameDuration, textureToFrames(
                    loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(Gdx.files.internal(filePath + idleFile + dir + anim)));
            idleAnimation.set(d.ordinal(), new Animation<>(frameDuration, textureToFrames(
                    loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(Gdx.files.internal(filePath + attackFile + dir + anim)));
            attackAnimation.set(d.ordinal(), new Animation<>(frameDuration, textureToFrames(
                    loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));
        }

        frameDuration = 0.25f;
        animationFrames = 3;
        String hitFile = "char_hit_";
        anim = "_anim_strip_3.png";

        for (var d : Direction.values()) {
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(Gdx.files.internal(filePath + hitFile + dir + anim)));
            hitAnimation.set(d.ordinal(), new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));
        }

        setCurrentAnimation();
    }

    private void putOffsets(){
        ObjectMap<Direction, Vector2> attackOffsets = new ObjectMap<>();
        attackOffsets.put(Direction.LEFT, new Vector2(-16, -16));
        attackOffsets.put(Direction.RIGHT, new Vector2(0, -16));
        attackOffsets.put(Direction.UP, new Vector2(-16, 0));
        attackOffsets.put(Direction.DOWN, new Vector2(-16, -16));
        offsets.put(State.ATTACK, attackOffsets);
    }
}
