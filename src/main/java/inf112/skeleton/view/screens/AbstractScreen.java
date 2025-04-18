package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.MyGame;

/**
 * An abstract class that leaves unneeded methods empty.
 */
public abstract class AbstractScreen implements Screen{
    protected static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    static {
        font.setUseIntegerPositions(false);
    }
    protected final MyGame game;
    protected ShapeRenderer shapeRenderer;

    protected SpriteBatch gameBatch;
    protected Viewport gameViewport;
    protected SpriteBatch uiBatch;
    protected Viewport uiViewport;

    private static final OrthographicCamera tempCamera = new OrthographicCamera();

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
        shapeRenderer = new ShapeRenderer();
        gameBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();
    }

    /**
     * Is called every frame. It's where we should decide what happens each frame.
     * @param deltaTime The time interval between each frame.
     */
    @Override
    public void render(float deltaTime){}

    protected void draw(SpriteBatch batch, TextureRegion tex, Vector2 p){
        batch.draw(tex, p.x, p.y);
    }

    protected void draw(Rectangle r){
        shapeRenderer.rect(r.x, r.y, r.width, r.height);
    }

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
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, tempCamera.viewportWidth, tempCamera.viewportHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Java garbage collector won't dispose unused objects and must be disposed manually.
     */
    @Override
    public void dispose(){
        shapeRenderer.dispose();
        gameBatch.dispose();
        uiBatch.dispose();
    }

    @Override
    public void resize(int width, int height){}
    @Override
    public void pause(){}
    @Override
    public void resume(){}
    @Override
    public void hide(){}
}
