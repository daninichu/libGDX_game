package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.util.Line;

public abstract class AbstractScreen extends ScreenAdapter{
    protected static final BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    protected static final Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
    static {
        font.setUseIntegerPositions(false);
    }
    private static final OrthographicCamera tempCamera = new OrthographicCamera();
    protected final MyGame game;

    protected Stage stage;
    protected SpriteBatch gameBatch, uiBatch;
    protected Viewport gameViewport, uiViewport;
    protected ShapeRenderer shapeRenderer;

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

    protected void draw(SpriteBatch batch, TextureRegion tex, Vector2 p){
        batch.draw(tex, p.x, p.y);
    }

    protected void draw(Shape2D shape, Color color){
        shapeRenderer.setColor(color);
        if(shape instanceof Rectangle r)
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        else if(shape instanceof Circle c)
            shapeRenderer.circle(c.x, c.y, c.radius);
        else if(shape instanceof Line l)
            shapeRenderer.line(l.x1, l.y1, l.x2, l.y2);
        else
            throw new IllegalArgumentException("Shape is not a Rectangle, Circle or Line.");
    }

    protected void draw(Shape2D shape, Color color, ShapeRenderer.ShapeType shapeType) {
        shapeRenderer.begin(shapeType);
        draw(shape, color);
        shapeRenderer.end();
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

    @Override
    public void resize(int width, int height){
        if(gameViewport != null)
            gameViewport.update(width, height);
        if(uiViewport != null)
            uiViewport.update(width, height, true);
    }

    /**
     * Java garbage collector won't dispose unused objects and must be disposed manually.
     */
    @Override
    public void dispose(){
        stage.dispose();
        shapeRenderer.dispose();
        gameBatch.dispose();
        uiBatch.dispose();
    }
}
