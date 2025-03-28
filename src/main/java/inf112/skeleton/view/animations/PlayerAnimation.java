package inf112.skeleton.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.Direction;

public class PlayerAnimation extends EntityAnimation {
    public PlayerAnimation() {
        super();
        putOffsets();

        float frameDuration = 0.125f;

        int animationFrames = 6;

        String filePath = "Char_Sprites/char_";
        String anim = "_anim_strip_6.png";

        for (var d : Direction.values()) {
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(Gdx.files.internal(filePath + "run_" + dir + anim)));
            animations.get(State.WALKING).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(Gdx.files.internal(filePath + "idle_" + dir + anim)));
            animations.get(State.IDLE).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));

            loadedTextures.add(new Texture(Gdx.files.internal(filePath + "attack_" + dir + anim)));
            animations.get(State.ATTACK).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));
        }

        frameDuration = 0.125f;
        animationFrames = 3;
        anim = "_anim_strip_3.png";

        for (var d : Direction.values()) {
            String dir = d.toString().toLowerCase();
            loadedTextures.add(new Texture(Gdx.files.internal(filePath + "hit_" + dir + anim)));
            animations.get(State.HIT).put(d, new Animation<>(frameDuration, textureToFrames(
                loadedTextures.get(loadedTextures.size - 1), animationFrames
            )));
        }
//        for (var d : Direction.values()) {
//            String dir = d.toString().toLowerCase();
//            loadedTextures.add(new Texture(Gdx.files.internal("down-Sheet.png")));
//            animations.get(State.IDLE).put(d, new Animation<>(1,
//                new TextureRegion(new Texture(Gdx.files.internal("down-Sheet.png")), 0, 0, 32, 48)
//            ));
//            animations.get(State.WALKING).put(d, new Animation<>(frameDuration, textureToFrames(
//                loadedTextures.get(loadedTextures.size - 1), 6
//            )));
//            animations.get(State.ATTACK).put(d, new Animation<>(frameDuration, textureToFrames(
//                loadedTextures.get(loadedTextures.size - 1), 6
//            )));
//            animations.get(State.HIT).put(d, new Animation<>(frameDuration, textureToFrames(
//                loadedTextures.get(loadedTextures.size - 1), 6
//            )));
//        }

        setCurrentAnimation();

        System.out.println(loadedTextures);
        System.out.println(loadedTextures.size);
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
