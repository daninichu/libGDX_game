package inf112.skeleton.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.util.Direction;

public class AnimationHandler implements Disposable {
    public enum State{
        IDLE, RUN, ATTACK, HIT, DEATH
    }
    private static final TextureAtlas atlas = new TextureAtlas("atlas/myAtlas.atlas");
    private ObjectMap<State, ObjectMap<Direction, Animation<TextureRegion>>> animations = new ObjectMap<>();
    protected ObjectMap<State, ObjectMap<Direction, Vector2>> offsets = new ObjectMap<>();
    private Animation<TextureRegion> currentAnimation;

    private Direction direction;
    private State state = State.IDLE;
    private float timer;

    /**
     * All png files must be in the form {@code [name]_[state]_[direction]_anim_strip_[num frames].png} in lowercase.
     */
    public AnimationHandler(String name, Direction initialDir) {
        for(State state : State.values()) {
            ObjectMap<Direction, Animation<TextureRegion>> animation = new ObjectMap<>();
            String stateStr = state.toString().toLowerCase();
            for(Direction dir : Direction.values()){
                String dirStr = dir.toString().toLowerCase();
                TextureAtlas.AtlasRegion reg = atlas.findRegion(name + "_" + stateStr + "_" + dirStr + "_anim_strip");
                if(reg == null)
                    reg = atlas.findRegion(name + "_" + stateStr + "_all_dir_anim_strip");
                if(reg != null)
                    animation.put(dir, new Animation<>(0.125f, textureToFrames(reg)));
            }
            animations.put(state, animation);
        }
        putOffsets();
        setDirection(initialDir);
    }

    protected void putOffsets(){}

    public TextureRegion getCurrentFrame() {
        if(currentAnimation == null)
            throw new NullPointerException("No animation for " + state + " " + direction);
        return currentAnimation.getKeyFrame(timer, true);
    }




    public void setFrameDuration(float duration, State state) {
        timer *= duration / currentAnimation.getFrameDuration();
        for(Direction direction : Direction.values())
            animations.get(state).get(direction).setFrameDuration(duration);
    }

    public Vector2 getOffset() {
        if(offsets.containsKey(state))
            return offsets.get(state).get(direction, new Vector2());
        return new Vector2();
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            setCurrentAnimation();
        }
    }

    /**
     * Animation is only reset if state is different from before.
     */
    public void setState(State state) {
        if (this.state != state) {
            timer = 0;
            this.state = state;
            setCurrentAnimation();
        }
    }

    public void update(float deltaTime) {
        timer += deltaTime;
    }

    private static TextureRegion[] textureToFrames(TextureAtlas.AtlasRegion reg) {
        return reg.split(reg.getRegionWidth() / reg.index, reg.getRegionHeight())[0];
    }

    private void setCurrentAnimation() {
        currentAnimation = animations.get(state).get(direction);
    }

    @Override
    public void dispose(){
        atlas.dispose();
    }
}
