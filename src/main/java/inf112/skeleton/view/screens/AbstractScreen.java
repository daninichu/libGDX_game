package inf112.skeleton.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.MyGame;

/**
 * An abstract class that leaves unneeded methods empty.
 */
public abstract class AbstractScreen implements Screen{
    /** Is needed to switch screens.*/
    protected final MyGame game;
    /** Responsible for drawing images and sprites.*/
    protected SpriteBatch batch;
    /** Responsible for drawing simple shapes.*/
    protected ShapeRenderer shapeRenderer;
    protected Viewport viewport;
    private OrthographicCamera tempCamera = new OrthographicCamera();

    protected float fadeDuration = 0.25f;
    protected float fadeTime;

    public AbstractScreen(MyGame game) {
        this.game = game;
    }

    /**
     * When switching to a different screen, the screen we just switched to will
     * call this method and refresh fields with new instances.
     */
    @Override
    public void show(){
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Is called every frame. It's where we should decide what happens each frame.
     * @param deltaTime The time interval between each frame.
     */
    @Override
    public void render(float deltaTime){}

    protected void fadeToBlack(float delta){
        fadeTime += delta;
        float alpha = Math.min(fadeTime / fadeDuration, 1.0f);
        blend(alpha);
    }

    protected void unfadeFromBlack(float delta) {
        fadeTime += delta;
        float alpha = Math.max((fadeDuration - fadeTime) / fadeDuration, 0);
        blend(alpha);
    }

    protected boolean resetFadeTimer(){
        if(fadeTime >= fadeDuration){
            fadeTime = 0;
            return true;
        }
        return false;
    }

    private void blend(float alpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        tempCamera.setToOrtho(false); // false to set the origin at the bottom left
        shapeRenderer.setProjectionMatrix(tempCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, tempCamera.viewportWidth, tempCamera.viewportHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height){}
    @Override
    public void pause(){}
    @Override
    public void resume(){}
    @Override
    public void hide(){}

    /**
     * Java garbage collector won't dispose unused objects and must be disposed manually.
     */
    @Override
    public void dispose(){
        batch.dispose();
        shapeRenderer.dispose();
    }
}
