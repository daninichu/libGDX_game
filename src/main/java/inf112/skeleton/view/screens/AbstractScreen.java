package inf112.skeleton.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * An abstract class that leaves unneeded methods empty.
 */
public abstract class AbstractScreen implements Screen{
    /** Is needed to switch screens.*/
    protected final Game game;
    /** Responsible for drawing images and sprites.*/
    protected SpriteBatch batch;
    /** Responsible for drawing simple shapes.*/
    protected ShapeRenderer shapeRenderer;

    public AbstractScreen(Game game) {
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
    public void render(float deltaTime){
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height){

    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void hide(){

    }

    /**
     * Java garbage collector won't dispose unused objects and must be disposed manually.
     */
    @Override
    public void dispose(){
        batch.dispose();
        shapeRenderer.dispose();
    }

}
