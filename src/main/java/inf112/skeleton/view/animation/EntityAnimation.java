package inf112.skeleton.view.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.Direction;


public abstract class EntityAnimation implements Disposable {
    public enum State{
        IDLE, RUN, ATTACK, HIT
    }
    protected ObjectMap<State, String> filePaths = new ObjectMap<>();
    protected ObjectMap<State, ObjectMap<Direction, Animation<TextureRegion>>> animations = new ObjectMap<>();
    protected ObjectMap<State, ObjectMap<Direction, Vector2>> offsets = new ObjectMap<>();
    protected Animation<TextureRegion> currentAnimation;

    protected Direction direction = Direction.DOWN;
    protected State state = State.IDLE;

    protected float timer;

    public EntityAnimation() {
        for(State state : State.values()){
            animations.put(state, new ObjectMap<>());
            filePaths.put(state, state.toString().toLowerCase() + "_");
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(timer, true);
    }

    public void setFrameDuration(float duration) {
        setFrameDuration(duration, state);
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

    /**
     * Animation is only reset if direction is different from before
     * depending on context, {@code direction} or {@code dirVec} is used to find texture direction.
     */
    public void setDirection(Direction direction, Vector2 dirVec) {
        if (this.direction != direction) {
            this.direction = direction;
            setCurrentAnimation();
        }
    }

    /**
     * Animation is only reset if state is different from before
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

    protected static TextureRegion[] textureToFrames(TextureAtlas.AtlasRegion reg) {
        return reg.split(reg.getRegionWidth() / reg.index, reg.getRegionHeight())[0];
    }

    protected void setCurrentAnimation() {
        currentAnimation = animations.get(state).get(direction);
    }
}
