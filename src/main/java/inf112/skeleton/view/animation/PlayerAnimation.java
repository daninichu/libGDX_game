package inf112.skeleton.view.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.Direction;

public class PlayerAnimation extends EntityAnimation {
    static String filePath = "Char_Sprites/char_";
//    private static Texture[][] idleSheet;
//    static{
//        for(var d : Direction.values()){
//            String dir = d.toString().toLowerCase();
//            idleSheet[0] = new Texture((filePath + "run_" + dir + "_anim_strip_6.png"));
//        }
//    }
    public PlayerAnimation() {
        super();
        putOffsets();

        float frameDuration = 0.125f;

        int animationFrames = 6;

        String anim = "_anim_strip_6.png";

        for (var d : Direction.values()){
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(filePath + "run_" + dir + anim));
            animations.get(State.WALKING).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(filePath + "idle_" + dir + anim));
            animations.get(State.IDLE).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(filePath + "attack_" + dir + anim));
            animations.get(State.ATTACK).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));
        }

        frameDuration = 0.125f;
        animationFrames = 3;
        anim = "_anim_strip_3.png";

        for (var d : Direction.values()) {
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(filePath + "hit_" + dir + anim));
            animations.get(State.HIT).put(d, new Animation<>(frameDuration, textureToFrames(
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
