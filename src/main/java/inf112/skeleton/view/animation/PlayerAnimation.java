package inf112.skeleton.view.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.Direction;

public class PlayerAnimation extends EntityAnimation {
    public PlayerAnimation() {
        super("char");
//        for (Direction d : Direction.values()){
//            String dir = d.toString().toLowerCase();
//            for(State state : State.values()){
//                if(state == State.DEATH)
//                    continue;
//                String file = filePaths.get(state);
//                TextureAtlas.AtlasRegion region = atlas.findRegion("char_" + file + dir + "_anim_strip");
//                animations.get(state).put(d, new Animation<>(0.125f, textureToFrames(region)));
//            }
//            TextureAtlas.AtlasRegion region = atlas.findRegion("char_death_all_dir_anim_strip");
//            animations.get(State.DEATH).put(d, new Animation<>(0.125f, textureToFrames(region)));
//        }

//        setCurrentAnimation();

        ObjectMap<Direction, Vector2> attackOffsets = new ObjectMap<>();
        attackOffsets.put(Direction.LEFT, new Vector2(-16, -16));
        attackOffsets.put(Direction.RIGHT, new Vector2(0, -16));
        attackOffsets.put(Direction.UP, new Vector2(-16, 0));
        attackOffsets.put(Direction.DOWN, new Vector2(-16, -16));
        offsets.put(State.ATTACK, attackOffsets);
    }
}
