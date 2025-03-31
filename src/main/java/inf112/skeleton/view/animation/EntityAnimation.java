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
        IDLE, RUN, ATTACK, HIT, DEATH
    }
    protected static final TextureAtlas atlas = new TextureAtlas("atlas/myAtlas.atlas");
    protected ObjectMap<State, ObjectMap<Direction, Animation<TextureRegion>>> animations = new ObjectMap<>();
    protected ObjectMap<State, ObjectMap<Direction, Vector2>> offsets = new ObjectMap<>();
    protected Animation<TextureRegion> currentAnimation;

    protected Direction direction = Direction.DOWN;
    protected State state = State.IDLE;

    protected float timer;

    public EntityAnimation(String name) {
        this(name, State.values(), Direction.values());
    }

    public EntityAnimation(String name, State[] states, Direction[] directions) {
        for(State state : states){
            ObjectMap<Direction, Animation<TextureRegion>> animation = new ObjectMap<>();
            String stateStr = state.toString().toLowerCase();

            for(Direction dir : directions){
                String dirStr = dir.toString().toLowerCase();
                String key = name + "_"+stateStr+"_"+dirStr+"_anim_strip";

                TextureAtlas.AtlasRegion region = atlas.findRegion(key);
                if(region == null){
                    key = name + "_"+stateStr+"_all_dir_anim_strip";
                }
                region = atlas.findRegion(key);
                if(region == null){
                    throw new RuntimeException("Couldn't find region: "+key);
                }
                animation.put(dir, new Animation<>(0.125f, textureToFrames(region)));
            }
            animations.put(state, animation);
        }
        setDirection(directions[0]);
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(timer, state != State.DEATH);
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
     * Animation is only reset if direction is different from before depending on
     * context, {@code direction} or {@code dirVec} is used to find texture direction.
     */
    public void setDirection(Direction direction) {
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

    @Override
    public void dispose(){
        atlas.dispose();
    }
}
