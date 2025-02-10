package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TiledMapWithPlayer extends ScreenAdapter {
    private final static boolean USE_METERS_INSTEAD_OF_PIXELS = true;

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Sprite player;

    private final ExtendViewport viewport;
    private final OrthogonalTiledMapRenderer tileRenderer;

    private final Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private final Stage ui = new Stage(new ScreenViewport());

    // Contains the world coordinates of the cursor
    private final Vector2 cursorPosition = new Vector2();

    public TiledMapWithPlayer() {
        // Loads the map and gets the tile size (width, but should be same anyway)
        var map = new TmxMapLoader().load("map/tileCollision.tmx");
        var tileSize = map.getProperties().get("tilewidth", Integer.class);

        player = new Sprite(new Texture("player.png"));

        // The renderer, by default, works with "pixels" as units
        // Therefore, if we want that 1 tile = 1 unit, we need to divide it by `1f / tilesize`.
        if (USE_METERS_INSTEAD_OF_PIXELS) {
            tileRenderer = new OrthogonalTiledMapRenderer(map, 1f / tileSize);
            viewport = new ExtendViewport(16, 9);
            // We want the player to be 1 tile big
            player.setSize(1f, 1f);
        } else {
            tileRenderer = new OrthogonalTiledMapRenderer(map, 1f);
            viewport = new ExtendViewport(16f * tileSize, 9f * tileSize);
        }

        setupUi();
    }

    // just some really basic ui that shows up in the top left corner
    private void setupUi() {
        var root = new Table();
        root.setFillParent(true);

        var topLeft = new Table();
        topLeft.add(new Label("Health: 3", skin)).pad(4);
        root.add(topLeft).expand().top().left();

        ui.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);

        // moves the player based on wasd input
        doPlayerInput();
        // zooms in and out
        doCameraInput();
        // moves the camera to the players positions
        followPlayerWithCamera();

        // updates the camera and tells opengl to use the games viewport
        viewport.apply();
        // updates the cursors position, needs to be done after `viewport.apply()` as the camera needs to be properly adjusted
        updateCursorPosition();

        // renders the tilemap
        tileRenderer.setView(((OrthographicCamera) viewport.getCamera()));
        tileRenderer.render();

        // renders the player
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        player.draw(batch);
        batch.end();

        // renders a square under the cursor to showcase that unprojecting works
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        var cursorSize = USE_METERS_INSTEAD_OF_PIXELS ? 0.1f : 2;
        shapeRenderer.rect(cursorPosition.x - cursorSize / 2f, cursorPosition.y - cursorSize / 2f, cursorSize, cursorSize);
        shapeRenderer.end();

        // applies the ui viewport and acts/renders it
        ui.getViewport().apply();
        ui.act();
        ui.draw();
    }

    private void updateCursorPosition() {
        // To convert from screen to world coordinates, set an vector2 to the screen coordinates, and then unproject it via the viewport
        cursorPosition.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(cursorPosition);
    }

    private void followPlayerWithCamera() {
        // To update the camera, just set the position of the camera to the players position
        // We need to add half of the players width/height so the camera is actually in the center of the player (and not in the bottom left)
        viewport.getCamera().position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);
    }

    private void doCameraInput() {
        var cam = ((OrthographicCamera) viewport.getCamera());
        var speed = 1f;

        // zooms in and out if you press space or control
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            cam.zoom += speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            cam.zoom -= speed * Gdx.graphics.getDeltaTime();

    }

    private void doPlayerInput() {
        var delta = Gdx.graphics.getDeltaTime();
        var speed = USE_METERS_INSTEAD_OF_PIXELS ? 10f : 100f;

        // moves the player up or down or left or right
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setPosition(player.getX(), player.getY() + speed * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setPosition(player.getX(), player.getY() - speed * delta);

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.setPosition(player.getX() - speed * delta, player.getY());
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setPosition(player.getX() + speed * delta, player.getY());
    }

    @Override
    public void resize(int width, int height) {
        // if you move the camera, you need to call (width, height) and not (width, height, true)
        viewport.update(width, height);
        // for ui, you need to make sure to call (width, height, true) so 0/0 is always in the bottom left
        ui.getViewport().update(width, height, true);
    }
}
